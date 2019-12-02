import {async, ComponentFixture, fakeAsync, TestBed, tick} from '@angular/core/testing';

import {RUNTIME_LOGS, RuntimeLogsComponent} from './runtime-logs.component';
import {ReplaySubject} from 'rxjs';
import {CommandLog} from 'projects/command/src/lib/entities/command-log';
import {Command} from 'projects/command/src/lib/entities/command';

describe('RuntimeLogsComponent', () => {
  let component: RuntimeLogsComponent;
  let fixture: ComponentFixture<RuntimeLogsComponent>;
  let logs: ReplaySubject<CommandLog>;

  beforeEach(async(() => {
    logs = new ReplaySubject();
    TestBed.configureTestingModule({
      declarations: [RuntimeLogsComponent],
      providers: [
        {provide: RUNTIME_LOGS, useValue: logs},
      ]
    })
      .overrideTemplate(RuntimeLogsComponent, '')
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RuntimeLogsComponent);
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
