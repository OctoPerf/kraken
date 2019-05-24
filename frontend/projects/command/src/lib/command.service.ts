import {EventEmitter, Injectable, OnDestroy} from '@angular/core';
import {Observable, Subscription} from 'rxjs';
import {EventBusService} from 'projects/event/src/lib/event-bus.service';
import {EventSourceService} from 'projects/tools/src/lib/event-source.service';
import {HttpClient} from '@angular/common/http';
import {flatMap, map, tap} from 'rxjs/operators';
import {NotificationEvent} from 'projects/notification/src/lib/notification-event';
import {BaseNotification} from 'projects/notification/src/lib/base-notification';
import {NotificationLevel} from 'projects/notification/src/lib/notification-level';
import {DialogService} from 'projects/dialog/src/lib/dialog.service';
import {ExecuteCommandDialogComponent} from 'projects/command/src/lib/command-dialogs/execute-command-dialog/execute-command-dialog.component';
import {LocalStorageService} from 'projects/tools/src/lib/local-storage.service';
import {Command} from 'projects/command/src/lib/entities/command';
import {ConfigurationService} from 'projects/commons/src/lib/config/configuration.service';
import * as _ from 'lodash';
import {StringToolsService} from 'projects/tools/src/lib/string-tools.service';
import {CommandLog} from 'projects/command/src/lib/entities/command-log';
import {OpenCommandLogsEvent} from 'projects/command/src/lib/entities/open-command-logs-event';
import {CommandConfigurationService} from 'projects/command/src/lib/command-configuration.service';
import {CommandLogEvent} from 'projects/command/src/lib/entities/command-log-event';
import {RetriesService} from 'projects/tools/src/lib/retries.service';
import {DurationToStringPipe} from 'projects/date/src/lib/duration-to-string.pipe';
import {Retry} from 'projects/tools/src/lib/retry';
import {DialogSize} from 'projects/dialog/src/lib/dialog-size';

@Injectable()
export class CommandService implements OnDestroy {

  private static readonly TRUNC = 50;

  private commandLabels: Map<string, { name: string, title: string }> = new Map([]);
  _eventSourceSubscription: Subscription;
  _retry: Retry;
  _destroyed = false;

  public commandLabelsChanged: EventEmitter<void> = new EventEmitter<void>();

  constructor(private eventBus: EventBusService,
              private eventSourceService: EventSourceService,
              private configuration: ConfigurationService,
              private commandConfiguration: CommandConfigurationService,
              private http: HttpClient,
              private dialogs: DialogService,
              private storage: LocalStorageService,
              private strings: StringToolsService,
              retries: RetriesService,
              private durationToString: DurationToStringPipe) {
    this._retry = retries.get();
    this.listen();
  }

  ngOnDestroy() {
    if (this._eventSourceSubscription) {
      this._eventSourceSubscription.unsubscribe();
    }
    this._destroyed = true;
  }

  execute(path: string): Observable<string> {
    const command = this.storage.getItem('command', new Command([Command.SHELL_0, Command.SHELL_1, 'echo $KEY'], {KEY: 'value'}));
    return this.dialogs.open(ExecuteCommandDialogComponent, DialogSize.SIZE_MD, {
      command,
      path: this.formatCommandPath(path)
    })
      .pipe(flatMap((_command: Command) => {
        _command.path = path;
        this.storage.setItem('command', _command);
        return this.executeCommand(_command);
      }));
  }

  executeScript(path: string, name: string): Observable<string> {
    const command: Command = new Command([
      '/bin/sh',
      '-c',
      `chmod +x ${name} && ./${name}`
    ], {}, path);
    return this.executeCommand(command);
  }

  executeCommand(command: Command): Observable<string> {
    command.applicationId = this.configuration.applicationId;
    return this.http.post(this.commandConfiguration.commandApiUrl('/execute'), command, {
      responseType: 'text',
    });
  }

  listen() {
    if (this._eventSourceSubscription) {
      this._eventSourceSubscription.unsubscribe();
    }
    // ApplicationId Header cannot be used here as it is not supported by SSE
    this._eventSourceSubscription = this.eventSourceService
      .newObservable(this.commandConfiguration.commandApiUrl(`/listen/${this.configuration.applicationId}`), {converter: JSON.parse})
      .subscribe(this._onMessage.bind(this), this._onError.bind(this), this._onError.bind(this));
  }

  _onMessage(log: CommandLog) {
    this._retry.reset();
    this.eventBus.publish(new CommandLogEvent(log));
  }

  _onError() {
    const delay = this._retry.getDelay();
    this.eventBus.publish(new NotificationEvent(
      new BaseNotification(
        `An error occurred while listening for command events. Will reconnect in ${this.durationToString.transform(delay)}.`,
        NotificationLevel.ERROR,
        null,
        {
          selector: 'lib-command-tabs-panel',
          busEvent: new OpenCommandLogsEvent()
        })));
    if (this._destroyed) {
      return;
    }
    setTimeout(this.listen.bind(this), delay);
  }

  cancel(commandId: string): Observable<boolean> {
    return this.http.delete(this.commandConfiguration.commandApiUrl('/cancel'), {
      responseType: 'text',
      params: {
        commandId
      }
    }).pipe(map(str => str === 'true'), tap((_value: boolean) => {
      if (!_value) {
        this.eventBus.publish(new NotificationEvent(
          new BaseNotification('Failed to cancel command. Please check server logs for errors',
            NotificationLevel.WARNING,
            null,
            {
              selector: 'lib-command-tabs-panel',
              busEvent: new OpenCommandLogsEvent()
            })));
      }
    }));
  }

  setCommandLabel(commandId: string, name: string, title: string) {
    this.commandLabels.set(commandId, {name, title});
    this.commandLabelsChanged.emit();
  }

  getCommandName(command: Command): string {
    if (this.commandLabels.has(command.id)) {
      return this.commandLabels.get(command.id).name;
    }
    return this.strings.truncEndString(_.join(command.command, ' '), CommandService.TRUNC);
  }

  getCommandTitle(command: Command): string {
    if (this.commandLabels.has(command.id)) {
      return this.commandLabels.get(command.id).title;
    }
    return `${_.join(command.command, ' ')} ${this.formatCommandPath(command.path)}`;
  }

  formatCommandPath(path: string): string {
    return path ? `in path '${path}'` : 'in root path';
  }

  removeCommandLabel(commandId: string) {
    this.commandLabels.delete(commandId);
  }
}
