import {TestBed} from '@angular/core/testing';

import {RuntimeTaskService} from './runtime-task.service';
import {BehaviorSubject} from 'rxjs';
import {RuntimeConfigurationService} from 'projects/runtime/src/lib/runtime-configuration.service';
import {HttpTestingController} from '@angular/common/http/testing';
import {EventBusService} from 'projects/event/src/lib/event-bus.service';
import {CoreTestModule} from 'projects/commons/src/lib/core/core.module.spec';
import {runtimeConfigurationServiceSpy} from 'projects/runtime/src/lib/runtime-configuration.service.spec';
import {testTask, testTasks} from 'projects/runtime/src/lib/entities/task.spec';
import {TaskCancelledEvent} from 'projects/runtime/src/lib/events/task-cancelled-event';
import {TaskExecutedEvent} from 'projects/runtime/src/lib/events/task-executed-event';
import {testExecutionContext} from 'projects/runtime/src/lib/entities/execution-context.spec';

export const runtimeTaskServiceSpy = () => {
  const spy = jasmine.createSpyObj('RuntimeTaskService', [
    'list',
    'cancel',
    'remove',
    'execute',
  ]);
  spy.tasksSubject = new BehaviorSubject([]);
  return spy;
};

describe('RuntimeTaskService', () => {
  let service: RuntimeTaskService;
  let httpTestingController: HttpTestingController;
  let eventBus: EventBusService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [CoreTestModule],
      providers: [
        {provide: RuntimeConfigurationService, useValue: runtimeConfigurationServiceSpy()},
        EventBusService,
        RuntimeTaskService,
      ]
    });
    eventBus = TestBed.get(EventBusService);
    service = TestBed.get(RuntimeTaskService);
    httpTestingController = TestBed.get(HttpTestingController);
  });

  afterEach(() => {
    httpTestingController.verify();
    service.ngOnDestroy();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should list', () => {
    const tasks = testTasks();
    service.list().subscribe(data => expect(data).toBe(tasks), () => fail('list failed'));
    const request = httpTestingController.expectOne('taskApiUrl/task/list');
    expect(request.request.method).toBe('GET');
    request.flush(tasks);
    expect(service.tasksSubject.value).toBe(tasks);
  });

  it('should cancel', () => {
    const publish = spyOn(eventBus, 'publish');
    const task = testTask();
    service.cancel(task).subscribe(data => expect(data).toBe(task.id), () => fail('cancel failed'));
    const request = httpTestingController.expectOne(req => req.url === 'taskApiUrl/task/cancel/' + task.type);
    expect(request.request.method).toBe('DELETE');
    expect(request.request.params.get('taskId')).toBe(task.id);
    request.flush(task.id);
    expect(publish).toHaveBeenCalledWith(new TaskCancelledEvent(task));
  });

  it('should remove', () => {
    const publish = spyOn(eventBus, 'publish');
    const task = testTask();
    service.remove(task).subscribe(data => expect(data).toBe(task.id), () => fail('remove failed'));
    const request = httpTestingController.expectOne(req => req.url === 'taskApiUrl/task/remove/' + task.type);
    expect(request.request.method).toBe('DELETE');
    expect(request.request.params.get('taskId')).toBe(task.id);
    request.flush(task.id);
    expect(publish).toHaveBeenCalledWith(new TaskCancelledEvent(task));
  });

  it('should execute', () => {
    const publish = spyOn(eventBus, 'publish');
    const taskId = 'taskId';
    const context = testExecutionContext();
    service.execute(context).subscribe(data => expect(data).toBe(taskId), () => fail('execute failed'));
    const request = httpTestingController.expectOne('taskApiUrl/task');
    expect(request.request.method).toBe('POST');
    expect(request.request.body).toBe(context);
    request.flush(taskId);
    expect(publish).toHaveBeenCalledWith(new TaskExecutedEvent(taskId, context));
  });

});
