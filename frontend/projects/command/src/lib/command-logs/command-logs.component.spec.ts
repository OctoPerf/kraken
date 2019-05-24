import {async, ComponentFixture, fakeAsync, TestBed, tick} from '@angular/core/testing';

import {ReplaySubject} from 'rxjs';
import {COMMAND_LOGS, CommandLogsComponent} from 'projects/command/src/lib/command-logs/command-logs.component';
import {CommandLog} from 'projects/command/src/lib/entities/command-log';
import {Command} from 'projects/command/src/lib/entities/command';

describe('CommandLogsComponent', () => {
  let component: CommandLogsComponent;
  let fixture: ComponentFixture<CommandLogsComponent>;
  let logs: ReplaySubject<CommandLog>;

  beforeEach(async(() => {
    logs = new ReplaySubject();
    TestBed.configureTestingModule({
      declarations: [CommandLogsComponent],
      providers: [
        {provide: COMMAND_LOGS, useValue: logs},
      ]
    })
      .overrideTemplate(CommandLogsComponent, '')
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CommandLogsComponent);
    component = fixture.componentInstance;
    component.codeEditor = jasmine.createSpyObj('codeEditor', ['appendText']);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
    component.ngAfterViewInit();
    component.ngOnDestroy();
  });

  it('should append text', fakeAsync(() => {
    logs.next({
      command: new Command(['java', '--version']),
      status: 'RUNNING',
      text: 'text',
    });
    tick(100);
    expect(component.codeEditor.appendText).toHaveBeenCalledWith('text');
  }));

  it('should append text CANCELLING', fakeAsync(() => {
    logs.next({
      command: new Command(['java', '--version']),
      status: 'CANCELLING',
      text: 'text',
    });
    tick(100);
    expect(component.codeEditor.appendText).toHaveBeenCalledWith('text');
  }));
});
