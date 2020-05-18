import {fakeAsync, TestBed, tick} from '@angular/core/testing';

import {SSEService} from './sse.service';
import {HttpTestingController} from '@angular/common/http/testing';
import {EventBusService} from 'projects/event/src/lib/event-bus.service';
import {CoreTestModule} from 'projects/commons/src/lib/core/core.module.spec';
import {eventBusSpy} from 'projects/event/src/lib/event-bus.service.spec';
import {ConfigurationService} from 'projects/commons/src/lib/config/configuration.service';
import {configurationServiceMock} from 'projects/commons/src/lib/config/configuration.service.spec';
import {RetriesService} from 'projects/tools/src/lib/retries.service';
import {retriesServiceSpy} from 'projects/tools/src/lib/retries.service.spec';
import {DurationToStringPipe} from 'projects/date/src/lib/duration-to-string.pipe';
import {NotificationEvent} from 'projects/notification/src/lib/notification-event';
import {Log} from 'projects/runtime/src/lib/entities/log';
import {testLog} from 'projects/runtime/src/lib/entities/log.spec';
import {eventSourceServiceSpy, eventSourceSpy} from 'projects/sse/src/lib/event-source.service.spec';
import {SSEConfigurationService} from 'projects/sse/src/lib/sse-configuration.service';
import {sseConfigurationServiceSpy} from 'projects/sse/src/lib/sse-configuration.service.spec';
import {SSEEvent} from 'projects/sse/src/lib/events/sse-event';
import {SSEWrapper} from 'projects/sse/src/lib/entities/sse-wrapper';
import {EventEmitter} from '@angular/core';
import {SecurityService} from 'projects/security/src/lib/security.service';
import {securityServiceSpy} from 'projects/security/src/lib/security.service.spec';
import SpyObj = jasmine.SpyObj;
import {of} from 'rxjs';
import {EventSourceService} from 'projects/sse/src/lib/event-source.service';

export const sseServiceSpy = () => {
  const spy = jasmine.createSpyObj('SSEService', [
    'watch',
  ]);
  spy.reconnected = new EventEmitter<void>();
  return spy;
};

describe('SSEService', () => {
  let service: SSEService;
  let httpTestingController: HttpTestingController;
  let eventBus: EventBusService;
  let eventSource: EventSource;
  let eventSourceService: SpyObj<EventSourceService>;
  let security: SpyObj<SecurityService>;

  beforeEach(() => {
    eventSource = eventSourceSpy();
    eventSourceService = eventSourceServiceSpy();
    security = securityServiceSpy();
    (security as any).token = of('token');
    TestBed.configureTestingModule({
      imports: [CoreTestModule],
      providers: [
        {provide: EventBusService, useValue: eventBusSpy()},
        {provide: ConfigurationService, useValue: configurationServiceMock()},
        {provide: SSEConfigurationService, useValue: sseConfigurationServiceSpy()},
        {provide: RetriesService, useValue: retriesServiceSpy()},
        {provide: SecurityService, useValue: security},
        {provide: EventSourceService, useValue: eventSourceService},
        SSEService,
        DurationToStringPipe,
      ]
    });
    eventBus = TestBed.inject(EventBusService);
    service = TestBed.inject(SSEService);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpTestingController.verify();
    service.ngOnDestroy();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should watch', () => {
    service.watch();
    expect(service._subscription).toBeTruthy();
    const subscription = service._subscription = jasmine.createSpyObj('_subscription', ['unsubscribe']);
    service.watch();
    expect(subscription.unsubscribe).toHaveBeenCalled();
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

  it('should send event on LOG message', () => {
    const log: Log = testLog();
    const wrapper: SSEWrapper = {type: 'LOG', value: log};
    service.next(wrapper);
    expect(service._retry.reset).toHaveBeenCalled();
    expect(eventBus.publish).toHaveBeenCalledWith(new SSEEvent(wrapper));
  });
});
