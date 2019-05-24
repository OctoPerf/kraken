import {AfterViewInit, Component, Inject, InjectionToken, OnDestroy, ViewChild} from '@angular/core';
import {CodeEditorComponent} from 'projects/editor/src/lib/code-editor/code-editor.component';
import {ReplaySubject, Subscription} from 'rxjs';
import {delay, filter} from 'rxjs/operators';
import {CommandLog} from 'projects/command/src/lib/entities/command-log';

export const COMMAND_LOGS = new InjectionToken<CommandLog>('CommandLogs');

@Component({
  selector: 'lib-command-logs',
  templateUrl: './command-logs.component.html',
  styleUrls: ['./command-logs.component.scss']
})
export class CommandLogsComponent implements OnDestroy, AfterViewInit {

  @ViewChild(CodeEditorComponent) codeEditor: CodeEditorComponent;

  private subscription: Subscription;

  constructor(@Inject(COMMAND_LOGS) private logs: ReplaySubject<CommandLog>) {
  }

  ngAfterViewInit() {
    this.subscription = this.logs
      .pipe(filter((_logs: CommandLog) => _logs.status === 'RUNNING' || _logs.status === 'CANCELLING'),
        delay(1)) // Gives time to the code editor for a refresh
      .subscribe(logs => this.codeEditor.appendText(logs.text));
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }

}
