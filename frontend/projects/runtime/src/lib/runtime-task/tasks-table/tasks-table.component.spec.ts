import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {TasksTableComponent} from 'projects/runtime/src/lib/runtime-task/tasks-table/tasks-table.component';
import {CoreTestModule} from 'projects/commons/src/lib/core/core.module.spec';
import {RuntimeTaskService} from 'projects/runtime/src/lib/runtime-task/runtime-task.service';
import {EventBusService} from 'projects/event/src/lib/event-bus.service';
import {runtimeTaskServiceSpy} from 'projects/runtime/src/lib/runtime-task/runtime-task.service.spec';
import {testTask, testTaskDone, testTasks} from 'projects/runtime/src/lib/entities/task.spec';
import {TaskSelectedEvent} from 'projects/runtime/src/lib/events/task-selected-event';
import {TasksRefreshEvent} from 'projects/runtime/src/lib/events/tasks-refresh-event';
import {of} from 'rxjs';
import * as _ from 'lodash';
import {DialogService} from 'projects/dialog/src/lib/dialog.service';
import {dialogsServiceSpy} from 'projects/dialog/src/lib/dialog.service.spec';
import {testContainers} from 'projects/runtime/src/lib/entities/container.spec';
import {Task} from 'projects/runtime/src/lib/entities/task';
import {ContainerStatusIsTerminalPipe} from 'projects/runtime/src/lib/runtime-task/container-status/container-status-is-terminal.pipe';
import SpyObj = jasmine.SpyObj;

describe('TaskTableComponent', () => {
  let component: TasksTableComponent;
  let fixture: ComponentFixture<TasksTableComponent>;
  let taskService: SpyObj<RuntimeTaskService>;
  let eventBus: EventBusService;
  let dialogs: SpyObj<DialogService>;
  const spyPipe = jasmine.createSpyObj('ContainerStatusIsTerminalPipe', [
    'transform',
  ]);

  beforeEach(async(() => {
    taskService = runtimeTaskServiceSpy();
    dialogs = dialogsServiceSpy();
    TestBed.configureTestingModule({
      imports: [CoreTestModule],
      declarations: [TasksTableComponent],
      providers: [
        {provide: RuntimeTaskService, useValue: taskService},
        {provide: DialogService, useValue: dialogs},
        {provide: ContainerStatusIsTerminalPipe, useValue: spyPipe},
        EventBusService,
      ]
    })
      .overrideTemplate(TasksTableComponent, '')
      .compileComponents();

    taskService.list.and.returnValue(of(testTasks()));
    eventBus = TestBed.inject(EventBusService);
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TasksTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  afterEach(() => {
    component.ngOnDestroy();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should fire TaskSelectedEvent on selection change', () => {
    const task = testTask();
    const publish = spyOn(eventBus, 'publish');
    component._selection.model.select(task);
    expect(publish).toHaveBeenCalledWith(new TaskSelectedEvent(task));
  });

  it('should fire TaskSelectedEvent on selection change', () => {
    const task = testTask();
    const publish = spyOn(eventBus, 'publish');
    component._selection.model.select(task);
    expect(publish).toHaveBeenCalledWith(new TaskSelectedEvent(task));
  });

  it('should set tasks on TasksRefreshEvent', () => {
    const tasks = testTasks();
    eventBus.publish(new TasksRefreshEvent(tasks));
    expect(component.loading).toBeFalsy();
    expect(component.dataSource.data).toBe(tasks);
    expect(component.dataSource.sort).toBe(component.sort);
  });

  it('should refresh on init', () => {
    const refresh = spyOn(component, 'refresh');
    component.ngOnInit();
    expect(refresh).toHaveBeenCalled();
  });

  it('should list tasks on refresh', () => {
    component.refresh();
    expect(taskService.list).toHaveBeenCalled();
  });

  it('should _match', () => {
    expect(component._match(testTask(), testTasks()[1])).toBeFalsy();
    expect(component._match(testTask(), testTask())).toBeTrue();
  });

  it('should set tasks update selection', () => {
    const tasks = testTasks();
    const task = _.cloneDeep(tasks[0]);
    component._selection.selection = task;
    component.tasks = tasks;
    expect(component._selection.selection).toBe(tasks[0]);
    expect(component._selection.selection).not.toBe(task);
  });

  it('should set tasks unselect', () => {
    const tasks = [];
    component._selection.selection = testTask();
    component.tasks = tasks;
    expect(component._selection.hasSelection).toBe(false);
  });

  it('should set tasks update selection first not done', () => {
    const tasks: Task[] = [
      {
        id: 'id1',
        startDate: 0,
        status: 'DONE',
        type: 'RUN',
        containers: testContainers(),
        expectedCount: 2,
        description: 'description1',
        applicationId: 'applicationId'
      },
      {
        id: 'id2',
        startDate: 0,
        status: 'READY',
        type: 'RUN',
        containers: testContainers(),
        expectedCount: 2,
        description: 'description2',
        applicationId: 'applicationId'
      }
    ];
    const task = _.cloneDeep(tasks[0]);
    component._selection.selection = task;
    component.tasks = tasks;
    expect(component._selection.selection).toBe(tasks[1]);
  });

  it('should set tasks update selection current done', () => {
    const tasks: Task[] = [
      {
        id: 'id1',
        startDate: 0,
        status: 'DONE',
        type: 'RUN',
        containers: testContainers(),
        expectedCount: 2,
        description: 'description1',
        applicationId: 'applicationId'
      },
      {
        id: 'id2',
        startDate: 0,
        status: 'DONE',
        type: 'RUN',
        containers: testContainers(),
        expectedCount: 2,
        description: 'description2',
        applicationId: 'applicationId'
      }
    ];
    const task = _.cloneDeep(tasks[0]);
    component._selection.selection = task;
    component.tasks = tasks;
    expect(component._selection.selection).toBe(tasks[0]);
  });

  it('should cancel task', () => {
    dialogs.confirm.and.returnValue(of(null));
    taskService.cancel.and.returnValue(of('taskId'));
    const task = testTask();
    component.cancel(task);
    expect(taskService.cancel).toHaveBeenCalledWith(task);
  });

  it('should cancel task with force', () => {
    dialogs.confirm.and.returnValue(of(null));
    taskService.cancel.and.returnValue(of('taskId'));
    const task = testTask();
    component.cancel(task, false);
    expect(taskService.cancel).toHaveBeenCalledWith(task);
  });

  it('should remove task', () => {
    taskService.remove.and.returnValue(of('taskId'));
    const task = testTask();
    component.remove(task);
    expect(taskService.remove).toHaveBeenCalledWith(task);
  });

  it('should delete task done and selected', () => {
    taskService.remove.and.returnValue(of(null));
    spyPipe.transform.and.returnValue(true);
    const task = testTaskDone();
    component._selection.selection = task;
    component.deleteSelection(true);
    expect(taskService.remove).toHaveBeenCalledWith(task);

  });

  it('should delete task selected', () => {
    spyPipe.transform.and.returnValue(false);
    const spy = spyOn(component, 'cancel');
    const task = testTask();
    component._selection.selection = task;
    component.deleteSelection(true);

    expect(spy).toHaveBeenCalledWith(task, true);
  });

});
