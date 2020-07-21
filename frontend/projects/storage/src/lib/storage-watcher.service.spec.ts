import {TestBed} from '@angular/core/testing';

import {StorageWatcherService} from './storage-watcher.service';
import {EventBusService} from 'projects/event/src/lib/event-bus.service';
import {NodeCreatedEvent} from 'projects/storage/src/lib/events/node-created-event';
import {NodeDeletedEvent} from 'projects/storage/src/lib/events/node-deleted-event';
import {NodeModifiedEvent} from 'projects/storage/src/lib/events/node-modified-event';
import {testStorageDirectoryNode} from 'projects/storage/src/lib/entities/storage-node.spec';
import {CoreTestModule} from 'projects/commons/src/lib/core/core.module.spec';
import {SSEEvent} from 'projects/sse/src/lib/events/sse-event';
import {StorageWatcherEvent} from 'projects/storage/src/lib/entities/storage-watcher-event';

describe('StorageWatcherService', () => {

  let service: StorageWatcherService;
  let eventBus: EventBusService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [CoreTestModule],
      providers: [
        EventBusService,
        StorageWatcherService,
      ]
    });
    eventBus = TestBed.inject(EventBusService);
    service = TestBed.inject(StorageWatcherService);
  });

  afterEach(() => {
    service.ngOnDestroy();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should handle CREATE event', () => {
    const watcherEvent: StorageWatcherEvent = {node: testStorageDirectoryNode(), event: 'CREATE'};
    const spy = spyOn(eventBus, 'publish').and.callThrough();
    eventBus.publish(new SSEEvent({type: 'NODE', value: watcherEvent}));
    expect(spy).toHaveBeenCalledWith(new NodeCreatedEvent(watcherEvent.node));
  });

  it('should handle DELETE event', () => {
    const watcherEvent: StorageWatcherEvent = {node: testStorageDirectoryNode(), event: 'DELETE'};
    const spy = spyOn(eventBus, 'publish').and.callThrough();
    eventBus.publish(new SSEEvent({type: 'NODE', value: watcherEvent}));
    expect(spy).toHaveBeenCalledWith(new NodeDeletedEvent(watcherEvent.node));
  });

  it('should handle MODIFY event', () => {
    const watcherEvent: StorageWatcherEvent = {node: testStorageDirectoryNode(), event: 'MODIFY'};
    const spy = spyOn(eventBus, 'publish').and.callThrough();
    eventBus.publish(new SSEEvent({type: 'NODE', value: watcherEvent}));
    expect(spy).toHaveBeenCalledWith(new NodeModifiedEvent(watcherEvent.node));
  });
});
