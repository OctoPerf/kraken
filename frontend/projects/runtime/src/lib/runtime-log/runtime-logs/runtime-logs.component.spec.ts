import {async, ComponentFixture, fakeAsync, TestBed, tick} from '@angular/core/testing';

import {RUNTIME_LOGS, RuntimeLogsComponent} from './runtime-logs.component';
import {ReplaySubject} from 'rxjs';
import {Log} from 'projects/runtime/src/lib/entities/log';

describe('RuntimeLogsComponent', () => {
  let component: RuntimeLogsComponent;
  let fixture: ComponentFixture<RuntimeLogsComponent>;
  let logs: ReplaySubject<Log>;

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
    logs.next({applicationId: 'applicationId', id: 'id', type: 'TASK', status: 'RUNNING', text: 'text'});
    tick(100);
    expect(component.codeEditor.appendText).toHaveBeenCalledWith('text\n');
  }));

  it('should append text CANCELLING', fakeAsync(() => {
    logs.next({applicationId: 'applicationId', id: 'id', type: 'TASK', status: 'CANCELLING', text: 'text'});
    tick(100);
    expect(component.codeEditor.appendText).toHaveBeenCalledWith('text\n');
  }));
});
