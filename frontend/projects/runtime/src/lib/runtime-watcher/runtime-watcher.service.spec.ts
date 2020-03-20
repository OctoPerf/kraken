import {TestBed} from '@angular/core/testing';

import {RuntimeWatcherService} from './runtime-watcher.service';
import {EventBusService} from 'projects/event/src/lib/event-bus.service';
import {CoreTestModule} from 'projects/commons/src/lib/core/core.module.spec';
import {testLog} from 'projects/runtime/src/lib/entities/log.spec';
import {LogEvent} from 'projects/runtime/src/lib/events/log-event';
import {testTasks} from 'projects/runtime/src/lib/entities/task.spec';
import {TasksRefreshEvent} from 'projects/runtime/src/lib/events/tasks-refresh-event';
import {SSEEvent} from 'projects/sse/src/lib/events/sse-event';

describe('RuntimeWatcherService', () => {
  let service: RuntimeWatcherService;
  let eventBus: EventBusService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [CoreTestModule],
      providers: [
        EventBusService,
        RuntimeWatcherService,
      ]
    });
    eventBus = TestBed.inject(EventBusService);
    service = TestBed.inject(RuntimeWatcherService);
  });

  afterEach(() => {
    service.ngOnDestroy();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should handle log event', () => {
    const spy = spyOn(eventBus, 'publish').and.callThrough();
    eventBus.publish(new SSEEvent({type: 'LOG', value: testLog()}));
    expect(spy).toHaveBeenCalledWith(new LogEvent(testLog()));
  });

  it('should handle tasks event', () => {
    const spy = spyOn(eventBus, 'publish').and.callThrough();
    eventBus.publish(new SSEEvent({type: 'TASKS', value: testTasks()}));
    expect(spy).toHaveBeenCalledWith(new TasksRefreshEvent(testTasks()));
  });
});
