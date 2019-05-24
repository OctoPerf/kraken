import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {EventBusService} from 'projects/event/src/lib/event-bus.service';
import {eventBusSpy} from 'projects/event/src/lib/event-bus.service.spec';
import {of, ReplaySubject} from 'rxjs';
import {CommandTabsPanelComponent} from 'projects/command/src/lib/command-tabs-panel/command-tabs-panel.component';
import {Command} from 'projects/command/src/lib/entities/command';
import {CommandLog} from 'projects/command/src/lib/entities/command-log';
import SpyObj = jasmine.SpyObj;
import {CommandService} from 'projects/command/src/lib/command.service';
import {commandServiceSpy} from 'projects/command/src/lib/command.service.spec';
import {OpenCommandLogsEvent} from 'projects/command/src/lib/entities/open-command-logs-event';

describe('CommandTabsPanelComponent', () => {
  let component: CommandTabsPanelComponent;
  let fixture: ComponentFixture<CommandTabsPanelComponent>;
  let eventBus: SpyObj<EventBusService>;
  let commandService: SpyObj<CommandService>;
  let command: Command;
  let initLogs: CommandLog;
  let runningLogs: CommandLog;
  let closedLogs: CommandLog;

  beforeEach(async(() => {
    eventBus = eventBusSpy();
    commandService = commandServiceSpy();
    command = new Command(['java', '--version']);
    initLogs = {command: command, status: 'INITIALIZED', text: ''};
    runningLogs = {command: command, status: 'RUNNING', text: 'text'};
    closedLogs = {command: command, status: 'CLOSED', text: 'text'};

    TestBed.configureTestingModule({
      declarations: [CommandTabsPanelComponent],
      providers: [
        {provide: EventBusService, useValue: eventBus},
        {provide: CommandService, useValue: commandService}
      ],
    })
      .overrideTemplate(CommandTabsPanelComponent, '')
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CommandTabsPanelComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should closeCommand', () => {
    component.commandTabs.push({} as any);
    component.selectedIndex = 1;
    component.closeCommand(0);
    expect(component.commandTabs.length).toBe(0);
    expect(component.selectedIndex).toBe(1);
  });

  it('should closeCommand and update selectedIndex', () => {
    component.commandTabs.push({} as any);
    component.selectedIndex = 0;
    component.closeCommand(0);
    expect(component.commandTabs.length).toBe(0);
    expect(component.selectedIndex).toBe(-1);
  });

  it('should stop command', () => {
    commandService.cancel.and.returnValue(of(true));
    component.stopCommand('commandId');
    expect(commandService.cancel).toHaveBeenCalledWith('commandId');
  });

  it('should _onCommandLog add tab INITIALIZED', () => {
    spyOn(component, '_addLogsTab');
    component._onCommandLog(initLogs);
    expect(component._addLogsTab).toHaveBeenCalledWith(initLogs);
  });

  it('should _onCommandLog add tab (unknown id)', () => {
    spyOn(component, '_addLogsTab');
    component._onCommandLog(runningLogs);
    expect(component._addLogsTab).toHaveBeenCalledWith(runningLogs);
  });

  it('should _onCommandLog update logs', () => {
    component.commandTabs.push(
      {
        content: null,
        lastLog: initLogs,
        logsSubject: jasmine.createSpyObj('ReplaySubject', ['next']),
      }
    );
    spyOn(component, '_addLogsTab');
    component._onCommandLog(runningLogs);
    expect(component.commandTabs[0].logsSubject.next).toHaveBeenCalledWith(runningLogs);
    expect(component.commandTabs[0].lastLog).toBe(runningLogs);
  });

  it('should _addLogsTab', () => {
    component._addLogsTab(initLogs);
    expect(component.commandTabs.length).toBe(1);
    expect(eventBus.publish).toHaveBeenCalledWith(new OpenCommandLogsEvent());
  });

  it('should close finished tasks', () => {
    component.commandTabs.push(
      {
        content: null,
        lastLog: initLogs,
        logsSubject: jasmine.createSpyObj('ReplaySubject', ['next']),
      }
    );
    component.commandTabs.push(
      {
        content: null,
        lastLog: runningLogs,
        logsSubject: jasmine.createSpyObj('ReplaySubject', ['next']),
      }
    );
    component.commandTabs.push(
      {
        content: null,
        lastLog: closedLogs,
        logsSubject: jasmine.createSpyObj('ReplaySubject', ['next']),
      }
    );
    component.selectedIndex = 2;
    component.closeFinishedTasks();
    expect(component.commandTabs.length).toBe(2);
    expect(component.selectedIndex).toBe(1);
  });
});
