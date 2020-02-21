import {fakeAsync, TestBed, tick} from '@angular/core/testing';

import {StorageWatcherService} from './storage-watcher.service';
import {EventBusService} from 'projects/event/src/lib/event-bus.service';
import {eventBusSpy} from 'projects/event/src/lib/event-bus.service.spec';
import {NodeCreatedEvent} from 'projects/storage/src/lib/events/node-created-event';
import {NodeDeletedEvent} from 'projects/storage/src/lib/events/node-deleted-event';
import {NotificationEvent} from 'projects/notification/src/lib/notification-event';
import {NodeModifiedEvent} from 'projects/storage/src/lib/events/node-modified-event';
import {testStorageDirectoryNode} from 'projects/storage/src/lib/entities/storage-node.spec';
import {QueryParamsToStringPipe} from 'projects/tools/src/lib/query-params-to-string.pipe';
import {StorageConfigurationService} from 'projects/storage/src/lib/storage-configuration.service';
import {storageConfigurationServiceSpy} from 'projects/storage/src/lib/storage-configuration.service.spec';
import {RetriesService} from 'projects/tools/src/lib/retries.service';
import {retriesServiceSpy} from 'projects/tools/src/lib/retries.service.spec';
import {DurationToStringPipe} from 'projects/date/src/lib/duration-to-string.pipe';
import {EventEmitter} from '@angular/core';

export const storageWatcherServiceSpy = () => {
  const spy = jasmine.createSpyObj('StorageWatcherService', [
    'watch',
  ]);
  spy.reconnected = new EventEmitter<void>();
  return spy;
};

describe('StorageWatcherService', () => {
  let service: StorageWatcherService;
  let eventBus: EventBusService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        {provide: StorageConfigurationService, useValue: storageConfigurationServiceSpy()},
        {provide: EventBusService, useValue: eventBusSpy()},
        {provide: RetriesService, useValue: retriesServiceSpy()},
        StorageWatcherService,
        QueryParamsToStringPipe,
        DurationToStringPipe,
      ]
    });
    service = TestBed.inject(StorageWatcherService);
    eventBus = TestBed.inject(EventBusService);
  });

  afterEach(() => {
    service.ngOnDestroy();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should create an event source', () => {
    service.watch();
    expect(service._subscription).toBeTruthy();
    const subscription = service._subscription = jasmine.createSpyObj('_subscription', ['unsubscribe']);
    service.watch();
    expect(subscription.unsubscribe).toHaveBeenCalled();
  });

  it('should handle CREATE message', () => {
    const node = testStorageDirectoryNode();
    service.next({node, event: 'CREATE'});
    expect(eventBus.publish).toHaveBeenCalledWith(new NodeCreatedEvent(node));
    expect(service._retry.reset).toHaveBeenCalled();
  });

  it('should handle DELETE message', () => {
    const node = testStorageDirectoryNode();
    service.next({node, event: 'DELETE'});
    expect(eventBus.publish).toHaveBeenCalledWith(new NodeDeletedEvent(node));
  });

  it('should handle MODIFY message', () => {
    const node = testStorageDirectoryNode();
    service.next({node, event: 'MODIFY'});
    expect(eventBus.publish).toHaveBeenCalledWith(new NodeModifiedEvent(node));
  });

  it('should handle error', fakeAsync(() => {
    const reconnected = spyOn(service.reconnected, 'emit');
    const watch = spyOn(service, 'watch');
    service.error(null);
    expect(eventBus.publish).toHaveBeenCalledWith(jasmine.any(NotificationEvent));
    tick(1000);
    expect(watch).toHaveBeenCalled();
    expect(reconnected).toHaveBeenCalled();
  }));

  it('should not handle error destroyed', fakeAsync(() => {
    const reconnected = spyOn(service.reconnected, 'emit');
    const watch = spyOn(service, 'watch');
    service.ngOnDestroy();
    service.complete();
    expect(eventBus.publish).toHaveBeenCalledWith(jasmine.any(NotificationEvent));
    tick(1000);
    expect(watch).not.toHaveBeenCalled();
    expect(reconnected).not.toHaveBeenCalled();
  }));
});
