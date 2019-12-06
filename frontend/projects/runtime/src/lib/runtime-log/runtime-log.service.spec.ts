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
import {NotificationEvent} from 'projects/notification/src/lib/notification-event';
import {BaseNotification} from 'projects/notification/src/lib/base-notification';
import {NotificationLevel} from 'projects/notification/src/lib/notification-level';
import {testTask} from 'projects/runtime/src/lib/entities/task.spec';
import {of} from 'rxjs';
import SpyObj = jasmine.SpyObj;

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
    eventBus = TestBed.get(EventBusService);
    service = TestBed.get(RuntimeLogService);
    runtimeContainerService = TestBed.get(RuntimeContainerService);
    runtimeTaskService = TestBed.get(RuntimeTaskService);
  });

  afterEach(() => {
    service.ngOnDestroy();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should handle task events', () => {
    let count = 0;
    service.logLabelsChanged.subscribe(() => count++);
    const taskId = 'taskId';
    const context = testExecutionContext();
    eventBus.publish(new TaskExecutedEvent(taskId, context));
    expect(service.label(taskId)).toEqual({name: 'description', title: 'RUN task description'});
    service.removeLabel(taskId);
    expect(service.label(taskId)).toEqual({name: taskId, title: taskId});
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
    expect(service.label(logsId)).toEqual({name: logsId, title: logsId});
    expect(count).toBe(1);
  });

  it('should cancel container log', () => {
    const log: Log = {applicationId: 'applicationId', id: 'id', type: 'CONTAINER', text: 'text', status: 'RUNNING'};
    service.cancel(log);
    expect(runtimeContainerService.detachLogs).toHaveBeenCalledWith('id');
  });

  it('should return label', () => {
    expect(service.label('unknown')).toEqual({name: 'unknown', title: 'unknown'});
  });
});
