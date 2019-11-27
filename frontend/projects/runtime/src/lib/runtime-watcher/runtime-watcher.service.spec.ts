import {fakeAsync, TestBed, tick} from '@angular/core/testing';

import {RuntimeWatcherService} from './runtime-watcher.service';
import {HttpTestingController} from '@angular/common/http/testing';
import {EventBusService} from 'projects/event/src/lib/event-bus.service';
import {eventSourceSpy} from 'projects/tools/src/lib/event-source.service.spec';
import {CoreTestModule} from 'projects/commons/src/lib/core/core.module.spec';
import {eventBusSpy} from 'projects/event/src/lib/event-bus.service.spec';
import {ConfigurationService} from 'projects/commons/src/lib/config/configuration.service';
import {configurationServiceMock} from 'projects/commons/src/lib/config/configuration.service.spec';
import {RetriesService} from 'projects/tools/src/lib/retries.service';
import {retriesServiceSpy} from 'projects/tools/src/lib/retries.service.spec';
import {DurationToStringPipe} from 'projects/date/src/lib/duration-to-string.pipe';
import {RuntimeConfigurationService} from 'projects/runtime/src/lib/runtime-configuration.service';
import {runtimeConfigurationServiceSpy} from 'projects/runtime/src/lib/runtime-configuration.service.spec';
import {NotificationEvent} from 'projects/notification/src/lib/notification-event';
import {Log} from 'projects/runtime/src/lib/entities/log';
import {testLog} from 'projects/runtime/src/lib/entities/log.spec';
import {LogEvent} from 'projects/runtime/src/lib/events/log-event';
import {Task} from 'projects/runtime/src/lib/entities/task';
import {testTasks} from 'projects/runtime/src/lib/entities/task.spec';
import {TasksRefreshEvent} from 'projects/runtime/src/lib/events/tasks-refresh-event';
import {QueryParamsToStringPipe} from 'projects/tools/src/lib/query-params-to-string.pipe';

export const runtimeWatcherServiceSpy = () => {
  const spy = jasmine.createSpyObj('RuntimeWatcherService', [
    'watch',
  ]);
  return spy;
};

describe('RuntimeWatcherService', () => {
  let service: RuntimeWatcherService;
  let httpTestingController: HttpTestingController;
  let eventBus: EventBusService;
  let eventSource: EventSource;

  beforeEach(() => {
    eventSource = eventSourceSpy();
    TestBed.configureTestingModule({
      imports: [CoreTestModule],
      providers: [
        {provide: EventBusService, useValue: eventBusSpy()},
        {provide: ConfigurationService, useValue: configurationServiceMock()},
        {provide: RuntimeConfigurationService, useValue: runtimeConfigurationServiceSpy()},
        {provide: RetriesService, useValue: retriesServiceSpy()},
        RuntimeWatcherService,
        DurationToStringPipe,
        QueryParamsToStringPipe,
      ]
    });
    eventBus = TestBed.get(EventBusService);
    service = TestBed.get(RuntimeWatcherService);
    httpTestingController = TestBed.get(HttpTestingController);
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
    expect(service._eventSourceSubscription).toBeTruthy();
    const subscription = service._eventSourceSubscription = jasmine.createSpyObj('_subscription', ['unsubscribe']);
    service.watch();
    expect(subscription.unsubscribe).toHaveBeenCalled();
  });

  it('should handle error', fakeAsync(() => {
    const watch = spyOn(service, 'watch');
    service.error(null);
    expect(eventBus.publish).toHaveBeenCalledWith(jasmine.any(NotificationEvent));
    tick(1000);
    expect(watch).toHaveBeenCalled();
  }));

  it('should not handle error destroyed', fakeAsync(() => {
    const watch = spyOn(service, 'watch');
    service.ngOnDestroy();
    service.complete();
    expect(eventBus.publish).toHaveBeenCalledWith(jasmine.any(NotificationEvent));
    tick(1000);
    expect(watch).not.toHaveBeenCalled();
  }));

  it('should send event on LOG message', () => {
    const log: Log = testLog();
    service.next({type: 'LOG', object: log});
    expect(service._retry.reset).toHaveBeenCalled();
    expect(eventBus.publish).toHaveBeenCalledWith(new LogEvent(log));
  });

  it('should send event on TASKS message', () => {
    const tasks: Task[] = testTasks();
    service.next({type: 'TASKS', object: tasks});
    expect(service._retry.reset).toHaveBeenCalled();
    expect(eventBus.publish).toHaveBeenCalledWith(new TasksRefreshEvent(tasks));
  });
});
