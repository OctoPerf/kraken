import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {TasksTableComponent} from 'projects/runtime/src/lib/runtime-task/tasks-table/tasks-table.component';
import {CoreTestModule} from 'projects/commons/src/lib/core/core.module.spec';
import {RuntimeTaskService} from 'projects/runtime/src/lib/runtime-task/runtime-task.service';
import {EventBusService} from 'projects/event/src/lib/event-bus.service';
import {runtimeTaskServiceSpy} from 'projects/runtime/src/lib/runtime-task/runtime-task.service.spec';
import {testTask, testTasks} from 'projects/runtime/src/lib/entities/task.spec';
import {TaskSelectedEvent} from 'projects/runtime/src/lib/events/task-selected-event';
import {TasksRefreshEvent} from 'projects/runtime/src/lib/events/tasks-refresh-event';
import {of} from 'rxjs';
import * as _ from 'lodash';
import SpyObj = jasmine.SpyObj;
import {DialogService} from 'projects/dialog/src/lib/dialog.service';
import {dialogsServiceSpy} from 'projects/dialog/src/lib/dialog.service.spec';
import {testContainers} from 'projects/runtime/src/lib/entities/container.spec';
import {Task} from 'projects/runtime/src/lib/entities/task';

describe('TaskTableComponent', () => {
  let component: TasksTableComponent;
  let fixture: ComponentFixture<TasksTableComponent>;
  let taskService: SpyObj<RuntimeTaskService>;
  let eventBus: EventBusService;
  let dialogs: SpyObj<DialogService>;

  beforeEach(async(() => {
    taskService = runtimeTaskServiceSpy();
    dialogs = dialogsServiceSpy();
    TestBed.configureTestingModule({
      imports: [CoreTestModule],
      declarations: [TasksTableComponent],
      providers: [
        {provide: RuntimeTaskService, useValue: taskService},
        {provide: DialogService, useValue: dialogs},
        EventBusService,
      ]
    })
      .overrideTemplate(TasksTableComponent, '')
      .compileComponents();

    taskService.list.and.returnValue(of(testTasks()));
    eventBus = TestBed.get(EventBusService);
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
    component._selection.select(task);
    expect(publish).toHaveBeenCalledWith(new TaskSelectedEvent(task));
  });

  it('should fire TaskSelectedEvent on selection change', () => {
    const task = testTask();
    const publish = spyOn(eventBus, 'publish');
    component._selection.select(task);
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

  it('should handle selection', () => {
    const task = testTask();
    expect(component.hasSelection).toBeFalsy();
    component.selection = task;
    expect(component.hasSelection).toBeTruthy();
    expect(component.isSelected(task)).toBeTruthy();
    component.selection = null;
    expect(component.hasSelection).toBeFalsy();
  });

  it('should set tasks update selection', () => {
    const tasks = testTasks();
    const task = _.cloneDeep(tasks[0]);
    component.selection = task;
    component.tasks = tasks;
    expect(component.selection).toBe(tasks[0]);
    expect(component.selection).not.toBe(task);
  });

  it('should set tasks unselect', () => {
    const tasks = [];
    component.selection = testTask();
    component.tasks = tasks;
    expect(component.hasSelection).toBeFalse();
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
        description: 'description1'
      },
      {
        id: 'id2',
        startDate: 0,
        status: 'READY',
        type: 'RUN',
        containers: testContainers(),
        expectedCount: 2,
        description: 'description2'
      }
    ];
    const task = _.cloneDeep(tasks[0]);
    component.selection = task;
    component.tasks = tasks;
    expect(component.selection).toBe(tasks[1]);
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
        description: 'description1'
      },
      {
        id: 'id2',
        startDate: 0,
        status: 'DONE',
        type: 'RUN',
        containers: testContainers(),
        expectedCount: 2,
        description: 'description2'
      }
    ];
    const task = _.cloneDeep(tasks[0]);
    component.selection = task;
    component.tasks = tasks;
    expect(component.selection).toBe(tasks[0]);
  });

  it('should cancel task', () => {
    dialogs.confirm.and.returnValue(of(null));
    taskService.cancel.and.returnValue(of('taskId'));
    const task = testTask();
    component.cancel(task);
    expect(taskService.cancel).toHaveBeenCalledWith(task);
  });
});
