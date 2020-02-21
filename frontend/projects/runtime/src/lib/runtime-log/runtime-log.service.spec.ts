import {TestBed} from '@angular/core/testing';

import {RuntimeLogService} from './runtime-log.service';
import {EventEmitter} from '@angular/core';
import {RuntimeTaskService} from 'projects/runtime/src/lib/runtime-task/runtime-task.service';
import {EventBusService} from 'projects/event/src/lib/event-bus.service';
import {CoreTestModule} from 'projects/commons/src/lib/core/core.module.spec';
import {RuntimeContainerService} from 'projects/runtime/src/lib/runtime-task/runtime-container.service';
import {runtimeContainerServiceSpy} from 'projects/runtime/src/lib/runtime-task/runtime-container.service.spec';
import {runtimeTaskServiceSpy} from 'projects/runtime/src/lib/runtime-task/runtime-task.service.spec';
import {TaskExecutedEvent} from 'projects/runtime/src/lib/events/task-executed-event';
import {testExecutionContext} from 'projects/runtime/src/lib/entities/execution-context.spec';
import {LogsAttachedEvent} from 'projects/runtime/src/lib/events/logs-attached-event';
import {testContainer} from 'projects/runtime/src/lib/entities/container.spec';
import {Log} from 'projects/runtime/src/lib/entities/log';
import SpyObj = jasmine.SpyObj;
import {of} from 'rxjs';
import {testTask} from 'projects/runtime/src/lib/entities/task.spec';
import {TaskCancelledEvent} from 'projects/runtime/src/lib/events/task-cancelled-event';

export const runtimeLogServiceSpy = () => {
  const spy = jasmine.createSpyObj('RuntimeLogService', [
    'cancel',
    'label',
    'removeLabel',
  ]);
  spy.logLabelsChanged = new EventEmitter<void>();
  return spy;
};

describe('RuntimeLogService', () => {
  let service: RuntimeLogService;
  let eventBus: EventBusService;
  let runtimeContainerService: SpyObj<RuntimeContainerService>;
  let runtimeTaskService: SpyObj<RuntimeTaskService>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [CoreTestModule],
      providers: [
        {provide: RuntimeContainerService, useValue: runtimeContainerServiceSpy()},
        {provide: RuntimeTaskService, useValue: runtimeTaskServiceSpy()},
        EventBusService,
        RuntimeLogService,
      ]
    });
    eventBus = TestBed.inject(EventBusService);
    service = TestBed.inject(RuntimeLogService);
    runtimeContainerService = TestBed.inject(RuntimeContainerService) as SpyObj<RuntimeContainerService>;
    runtimeTaskService = TestBed.inject(RuntimeTaskService) as SpyObj<RuntimeTaskService>;
  });

  afterEach(() => {
    service.ngOnDestroy();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should handle task executed event', () => {
    let count = 0;
    service.logLabelsChanged.subscribe(() => count++);
    const taskId = 'taskId';
    const context = testExecutionContext();
    eventBus.publish(new TaskExecutedEvent(taskId, context));
    expect(service.label(taskId)).toEqual({name: 'description', title: 'RUN task description'});
    service.removeLabel(taskId);
    expect(service.label(taskId)).toBeUndefined();
    expect(count).toBe(1);
  });

  it('should handle task cancel event', () => {
    let count = 0;
    service.logLabelsChanged.subscribe(() => count++);
    const task = testTask();
    eventBus.publish(new TaskCancelledEvent(task));
    expect(service.label(task.id)).toEqual({name: 'description', title: 'RUN task description'});
    service.removeLabel(task.id);
    expect(service.label(task.id)).toBeUndefined();
    expect(count).toBe(1);
  });

  it('should handle container events', () => {
    let count = 0;
    service.logLabelsChanged.subscribe(() => count++);
    const logsId = 'logsId';
    const container = testContainer();
    eventBus.publish(new LogsAttachedEvent(logsId, container));
    expect(service.label(logsId)).toEqual({name: 'label', title: 'name on hostId'});
    service.removeLabel(logsId);
    expect(service.label(logsId)).toBeUndefined();
    expect(count).toBe(1);
  });

  it('should cancel container log', () => {
    runtimeContainerService.detachLogs.and.returnValue(of('true'));
    const log: Log = {applicationId: 'applicationId', id: 'id', type: 'CONTAINER', text: 'text', status: 'RUNNING'};
    service.cancel(log);
    expect(runtimeContainerService.detachLogs).toHaveBeenCalledWith('id');
  });

});
