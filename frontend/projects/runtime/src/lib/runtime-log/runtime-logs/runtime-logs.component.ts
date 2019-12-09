import {AfterViewInit, Component, Inject, InjectionToken, OnDestroy, ViewChild} from '@angular/core';
import {CodeEditorComponent} from 'projects/editor/src/lib/code-editor/code-editor.component';
import {ReplaySubject, Subscription} from 'rxjs';
import {Log} from 'projects/runtime/src/lib/entities/log';
import {delay, filter} from 'rxjs/operators';

export const RUNTIME_LOGS = new InjectionToken<Log>('RuntimeLogs');

@Component({
  selector: 'lib-runtime-logs',
  templateUrl: './runtime-logs.component.html',
  styleUrls: ['./runtime-logs.component.scss']
})
export class RuntimeLogsComponent implements OnDestroy, AfterViewInit {

  @ViewChild(CodeEditorComponent, {static: true}) codeEditor: CodeEditorComponent;

  private subscription: Subscription;

  constructor(@Inject(RUNTIME_LOGS) private logs: ReplaySubject<Log>) {
  }

  ngAfterViewInit() {
    this.subscription = this.logs
      .pipe(filter((_logs: Log) => _logs.status === 'RUNNING' || _logs.status === 'CANCELLING'),
        delay(1)) // Gives time to the code editor for a refresh
      .subscribe(logs => this.codeEditor.appendText('\n\r' + logs.text));
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }

}
