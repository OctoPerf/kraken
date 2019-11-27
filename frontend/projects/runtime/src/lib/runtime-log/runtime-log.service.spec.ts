import {TestBed} from '@angular/core/testing';

import {RuntimeLogService} from './runtime-log.service';
import {EventEmitter} from '@angular/core';
import {RuntimeTaskService} from 'projects/runtime/src/lib/runtime-task/runtime-task.service';
import {HttpTestingController} from '@angular/common/http/testing';
import {EventBusService} from 'projects/event/src/lib/event-bus.service';
import {CoreTestModule} from 'projects/commons/src/lib/core/core.module.spec';
import {RuntimeConfigurationService} from 'projects/runtime/src/lib/runtime-configuration.service';
import {runtimeConfigurationServiceSpy} from 'projects/runtime/src/lib/runtime-configuration.service.spec';
import {RuntimeContainerService} from 'projects/runtime/src/lib/runtime-task/runtime-container.service';
import {DialogService} from 'projects/dialog/src/lib/dialog.service';
import SpyObj = jasmine.SpyObj;
import {runtimeContainerServiceSpy} from 'projects/runtime/src/lib/runtime-task/runtime-container.service.spec';
import {runtimeTaskServiceSpy} from 'projects/runtime/src/lib/runtime-task/runtime-task.service.spec';
import {dialogsServiceSpy} from 'projects/dialog/src/lib/dialog.service.spec';
import {TaskExecutedEvent} from 'projects/runtime/src/lib/events/task-executed-event';
import {testExecutionContext} from 'projects/runtime/src/lib/entities/execution-context.spec';
import {TaskCancelledEvent} from 'projects/runtime/src/lib/events/task-cancelled-event';
import {LogsAttachedEvent} from 'projects/runtime/src/lib/events/logs-attached-event';
import {testContainer} from 'projects/runtime/src/lib/entities/container.spec';
import {LogsDetachedEvent} from 'projects/runtime/src/lib/events/logs-detached-event';
import {Log} from 'projects/runtime/src/lib/entities/log';
import * as _ from 'lodash';
import {flatMap} from 'rxjs/operators';
import {NotificationEvent} from 'projects/notification/src/lib/notification-event';
import {BaseNotification} from 'projects/notification/src/lib/base-notification';
import {NotificationLevel} from 'projects/notification/src/lib/notification-level';
import {testLog} from 'projects/runtime/src/lib/entities/log.spec';
import {testTask} from 'projects/runtime/src/lib/entities/task.spec';
import {of} from 'rxjs';

export const runtimeLogServiceSpy = () => {
  const spy = jasmine.createSpyObj('RuntimeLogService', [
    'cancel',
    'label',
  ]);
  spy.logLabelsChanged = new EventEmitter<void>();
  return spy;
};

describe('RuntimeLogService', () => {
  let service: RuntimeLogService;
  let eventBus: EventBusService;
  let runtimeContainerService: SpyObj<RuntimeContainerService>;
  let runtimeTaskService: SpyObj<RuntimeTaskService>;
  let dialogs: SpyObj<DialogService>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [CoreTestModule],
      providers: [
        {provide: RuntimeContainerService, useValue: runtimeContainerServiceSpy()},
        {provide: RuntimeTaskService, useValue: runtimeTaskServiceSpy()},
        {provide: DialogService, useValue: dialogsServiceSpy()},
        EventBusService,
        RuntimeLogService,
      ]
    });
    eventBus = TestBed.get(EventBusService);
    service = TestBed.get(RuntimeLogService);
    runtimeContainerService = TestBed.get(RuntimeContainerService);
    runtimeTaskService = TestBed.get(RuntimeTaskService);
    dialogs = TestBed.get(DialogService);
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
    eventBus.publish(new TaskCancelledEvent(taskId));
    expect(service.label(taskId)).toBeUndefined();
    expect(count).toBe(2);
  });

  it('should handle container events', () => {
    let count = 0;
    service.logLabelsChanged.subscribe(() => count++);
    const logsId = 'logsId';
    const container = testContainer();
    eventBus.publish(new LogsAttachedEvent(logsId, container));
    expect(service.label(logsId)).toEqual({name: 'label', title: 'name on hostId'});
    eventBus.publish(new LogsDetachedEvent(logsId));
    expect(service.label(logsId)).toBeUndefined();
    expect(count).toBe(2);
  });

  it('should cancel container log', () => {
    const log: Log = {applicationId: 'applicationId', id: 'id', type: 'CONTAINER', text: 'text'};
    service.cancel(log);
    expect(runtimeContainerService.detachLogs).toHaveBeenCalledWith('id');
  });

  it('should cancel task log', () => {
    const task = testTask();
    const log: Log = {applicationId: 'applicationId', id: task.id, type: 'TASK', text: 'text'};
    runtimeTaskService.tasksSubject.next([task]);
    dialogs.confirm.and.returnValue(of(null));
    service.cancel(log);
    expect(runtimeTaskService.cancel).toHaveBeenCalledWith(task.id, task.type);
  });

  it('should cancel task log fail', () => {
    const publish = spyOn(eventBus, 'publish');
    const task = testTask();
    const log: Log = {applicationId: 'applicationId', id: task.id, type: 'TASK', text: 'text'};
    dialogs.confirm.and.returnValue(of(null));
    service.cancel(log);
    expect(publish).toHaveBeenCalledWith(new NotificationEvent(
      new BaseNotification(
        `Cannot find task with id ${log.id}.`,
        NotificationLevel.ERROR,
      )));
  });
});
