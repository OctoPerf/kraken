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
import SpyObj = jasmine.SpyObj;

describe('TaskTableComponent', () => {
  let component: TasksTableComponent;
  let fixture: ComponentFixture<TasksTableComponent>;
  let taskService: SpyObj<RuntimeTaskService>;
  let eventBus: EventBusService;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [CoreTestModule],
      declarations: [TasksTableComponent],
      providers: [
        {provide: RuntimeTaskService, useValue: runtimeTaskServiceSpy()},
        EventBusService,
      ]
    })
      .overrideTemplate(TasksTableComponent, '')
      .compileComponents();

    taskService = TestBed.get(RuntimeTaskService);
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
});
