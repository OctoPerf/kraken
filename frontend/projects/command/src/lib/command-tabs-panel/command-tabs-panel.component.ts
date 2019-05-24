import {Component, Injector, OnDestroy, OnInit} from '@angular/core';
import {EventBusService} from 'projects/event/src/lib/event-bus.service';
import {WindowService} from 'projects/tools/src/lib/window.service';
import {ComponentPortal, PortalInjector} from '@angular/cdk/portal';
import * as _ from 'lodash';
import {ReplaySubject, Subscription} from 'rxjs';
import {COMMAND_LOGS, CommandLogsComponent} from 'projects/command/src/lib/command-logs/command-logs.component';
import {CommandLog} from 'projects/command/src/lib/entities/command-log';
import {CommandService} from 'projects/command/src/lib/command.service';
import {OpenCommandLogsEvent} from 'projects/command/src/lib/entities/open-command-logs-event';
import {CommandLogEvent} from 'projects/command/src/lib/entities/command-log-event';
import {TabSelectedEvent} from 'projects/tabs/src/lib/tab-selected-event';
import {filter, map} from 'rxjs/operators';

export interface CommandTab {
  content: ComponentPortal<CommandLogsComponent>;
  lastLog: CommandLog;
  logsSubject: ReplaySubject<CommandLog>;
}

@Component({
  selector: 'lib-command-tabs-panel',
  templateUrl: './command-tabs-panel.component.html',
  styleUrls: ['./command-tabs-panel.component.scss']
})
export class CommandTabsPanelComponent implements OnDestroy {

  commandTabs: CommandTab[] = [];
  selectedIndex = 0;
  private subscription: Subscription;

  constructor(public windowService: WindowService,
              private eventBus: EventBusService,
              private service: CommandService,
              private injector: Injector) {
    this.subscription = this.eventBus.of<CommandLogEvent>(CommandLogEvent.CHANNEL)
      .pipe(map(event => event.log))
      .subscribe(this._onCommandLog.bind(this));
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }

  closeCommand(index: number) {
    this.commandTabs.splice(index, 1);
    if (this.selectedIndex === index) {
      this._updateSelectedIndex(index);
    }
  }

  stopCommand(commandId: string) {
    this.service.cancel(commandId).subscribe();
  }

  closeFinishedTasks() {
    _.remove(this.commandTabs, (tab: CommandTab) => {
      return tab.lastLog.status === 'CLOSED';
    });
    this._updateSelectedIndex(this.selectedIndex);
  }

  _updateSelectedIndex(index: number) {
    this.selectedIndex = Math.min(Math.max(index, 0), this.commandTabs.length - 1);
  }

  _onCommandLog(log: CommandLog) {
    if (log.status === 'INITIALIZED') {
      this._addLogsTab(log);
      return;
    }
    const commandIndex: number = _.findIndex(this.commandTabs, commandTab => commandTab.lastLog.command.id === log.command.id);
    if (commandIndex === -1) {
      this._addLogsTab(log);
      return;
    }
    this.commandTabs[commandIndex].logsSubject.next(log);
    this.commandTabs[commandIndex].lastLog = log;
  }

  _addLogsTab(log: CommandLog) {
    const injectorTokens = new WeakMap();
    const logsSubject = new ReplaySubject<CommandLog>(10);
    injectorTokens.set(COMMAND_LOGS, logsSubject);
    const content = new ComponentPortal(CommandLogsComponent, null, new PortalInjector(this.injector, injectorTokens));
    const command: CommandTab = {
      content,
      logsSubject,
      lastLog: log,
    };
    logsSubject.next(log);
    this.commandTabs.push(command);
    this.selectedIndex = this.commandTabs.length - 1;
    this.eventBus.publish(new OpenCommandLogsEvent());
  }

}
