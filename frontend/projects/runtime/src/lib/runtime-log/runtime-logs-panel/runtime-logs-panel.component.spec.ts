import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {RuntimeLogsPanelComponent} from './runtime-logs-panel.component';
import {EventBusService} from 'projects/event/src/lib/event-bus.service';
import {RuntimeLogService} from 'projects/runtime/src/lib/runtime-log/runtime-log.service';
import {Log} from 'projects/runtime/src/lib/entities/log';
import {runtimeLogServiceSpy} from 'projects/runtime/src/lib/runtime-log/runtime-log.service.spec';
import {OpenLogsEvent} from 'projects/runtime/src/lib/events/open-logs-event';
import SpyObj = jasmine.SpyObj;

describe('RuntimeLogsPanelComponent', () => {
  let component: RuntimeLogsPanelComponent;
  let fixture: ComponentFixture<RuntimeLogsPanelComponent>;
  let eventBus: EventBusService;
  let service: SpyObj<RuntimeLogService>;
  let runningLogs: Log;
  let closedLogs: Log;

  beforeEach(async(() => {
    service = runtimeLogServiceSpy();
    runningLogs = {applicationId: 'applicationId', id: 'id', type: 'TASK', status: 'RUNNING', text: 'text'};
    closedLogs = {applicationId: 'applicationId', id: 'id', type: 'TASK', status: 'CLOSED', text: ''};

    TestBed.configureTestingModule({
      declarations: [RuntimeLogsPanelComponent],
      providers: [
        {provide: RuntimeLogService, useValue: service},
        EventBusService
      ],
    })
      .overrideTemplate(RuntimeLogsPanelComponent, '')
      .compileComponents();
    eventBus = TestBed.get(EventBusService);
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RuntimeLogsPanelComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should close', () => {
    component.tabs.push({} as any);
    component.selectedIndex = 1;
    component.close(0);
    expect(component.tabs.length).toBe(0);
    expect(component.selectedIndex).toBe(1);
  });

  it('should close and update selectedIndex', () => {
    component.tabs.push({} as any);
    component.selectedIndex = 0;
    component.close(0);
    expect(component.tabs.length).toBe(0);
    expect(component.selectedIndex).toBe(-1);
  });

  it('should stop logs', () => {
    component.stop(closedLogs);
    expect(service.cancel).toHaveBeenCalledWith(closedLogs);
  });

  it('should _onLog add tab (unknown id)', () => {
    spyOn(component, '_addLogsTab');
    component._onLog(runningLogs);
    expect(component._addLogsTab).toHaveBeenCalledWith(runningLogs);
  });

  it('should _onLog update logs', () => {
    component.tabs.push(
      {
        content: null,
        lastLog: runningLogs,
        logsSubject: jasmine.createSpyObj('ReplaySubject', ['next']),
      }
    );
    spyOn(component, '_addLogsTab');
    component._onLog(runningLogs);
    expect(component.tabs[0].logsSubject.next).toHaveBeenCalledWith(runningLogs);
    expect(component.tabs[0].lastLog).toBe(runningLogs);
  });

  it('should _addLogsTab', () => {
    const publish = spyOn(eventBus, 'publish');
    component._addLogsTab(runningLogs);
    expect(component.tabs.length).toBe(1);
    expect(publish).toHaveBeenCalledWith(new OpenLogsEvent());
  });

  it('should close finished tasks', () => {
    component.tabs.push(
      {
        content: null,
        lastLog: runningLogs,
        logsSubject: jasmine.createSpyObj('ReplaySubject', ['next']),
      }
    );
    component.tabs.push(
      {
        content: null,
        lastLog: runningLogs,
        logsSubject: jasmine.createSpyObj('ReplaySubject', ['next']),
      }
    );
    component.tabs.push(
      {
        content: null,
        lastLog: closedLogs,
        logsSubject: jasmine.createSpyObj('ReplaySubject', ['next']),
      }
    );
    component.selectedIndex = 2;
    component.closeFinishedTasks();
    expect(component.tabs.length).toBe(2);
    expect(component.selectedIndex).toBe(1);
  });
});
