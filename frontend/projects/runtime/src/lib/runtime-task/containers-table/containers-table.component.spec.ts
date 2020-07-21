import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {ContainersTableComponent} from './containers-table.component';
import {EventBusService} from 'projects/event/src/lib/event-bus.service';
import {CoreTestModule} from 'projects/commons/src/lib/core/core.module.spec';
import {RuntimeContainerService} from 'projects/runtime/src/lib/runtime-task/runtime-container.service';
import {runtimeContainerServiceSpy} from 'projects/runtime/src/lib/runtime-task/runtime-container.service.spec';
import {TaskSelectedEvent} from 'projects/runtime/src/lib/events/task-selected-event';
import {testTask} from 'projects/runtime/src/lib/entities/task.spec';
import {of} from 'rxjs';
import SpyObj = jasmine.SpyObj;
import {RuntimeHostService} from 'projects/runtime/src/lib/runtime-host/runtime-host.service';
import {runtimeHostServiceSpy} from 'projects/runtime/src/lib/runtime-host/runtime-host.service.spec';

describe('ContainersTableComponent', () => {
  let component: ContainersTableComponent;
  let fixture: ComponentFixture<ContainersTableComponent>;
  let containerService: SpyObj<RuntimeContainerService>;
  let hostsService: SpyObj<RuntimeHostService>;
  let eventBus: EventBusService;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [CoreTestModule],
      declarations: [ContainersTableComponent],
      providers: [
        {provide: RuntimeContainerService, useValue: runtimeContainerServiceSpy()},
        {provide: RuntimeHostService, useValue: runtimeHostServiceSpy()},
        EventBusService,
      ]
    })
      .overrideTemplate(ContainersTableComponent, '')
      .compileComponents();

    containerService = TestBed.inject(RuntimeContainerService) as SpyObj<RuntimeContainerService>;
    hostsService = TestBed.inject(RuntimeHostService) as SpyObj<RuntimeHostService>;
    eventBus = TestBed.inject(EventBusService);

    hostsService.hosts.and.returnValue(of(null));
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ContainersTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  afterEach(() => {
    component.ngOnDestroy();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
    expect(hostsService.hosts).toHaveBeenCalled();
  });

  it('should handle task selection', () => {
    const task = testTask();
    eventBus.publish(new TaskSelectedEvent(task));
    expect(component.dataSource.data).toBe(task.containers);
    expect(component.dataSource.sort).toBe(component.sort);
  });

  it('should handle task unselection', () => {
    eventBus.publish(new TaskSelectedEvent(null));
    expect(component.dataSource.data).toEqual([]);
    expect(component.dataSource.sort).toBe(component.sort);
  });

  it('should attach logs', () => {
    containerService.attachLogs.and.returnValue(of('id'));
    const task = testTask();
    eventBus.publish(new TaskSelectedEvent(task));
    const container = task.containers[0];
    component.logs(container);
    expect(containerService.attachLogs).toHaveBeenCalledWith(task.id, container);
  });
});
