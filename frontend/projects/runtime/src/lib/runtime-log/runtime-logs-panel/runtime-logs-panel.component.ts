import {Component, Injector, OnDestroy, OnInit} from '@angular/core';
import {ReplaySubject, Subscription} from 'rxjs';
import {WindowService} from 'projects/tools/src/lib/window.service';
import {EventBusService} from 'projects/event/src/lib/event-bus.service';
import {map} from 'rxjs/operators';
import * as _ from 'lodash';
import {CommandLog} from 'projects/command/src/lib/entities/command-log';
import {ComponentPortal, PortalInjector} from '@angular/cdk/portal';
import {LogEvent} from 'projects/runtime/src/lib/events/log-event';
import {Log} from 'projects/runtime/src/lib/entities/log';
import {
  RUNTIME_LOGS,
  RuntimeLogsComponent
} from 'projects/runtime/src/lib/runtime-log/runtime-logs/runtime-logs.component';
import {RuntimeLogService} from 'projects/runtime/src/lib/runtime-log/runtime-log.service';
import {OpenLogsEvent} from 'projects/runtime/src/lib/events/open-logs-event';

export interface LogTab {
  content: ComponentPortal<RuntimeLogsComponent>;
  lastLog: Log;
  logsSubject: ReplaySubject<Log>;
}

@Component({
  selector: 'lib-runtime-logs-panel',
  templateUrl: './runtime-logs-panel.component.html',
  styleUrls: ['./runtime-logs-panel.component.scss']
})
export class RuntimeLogsPanelComponent implements OnDestroy {

  tabs: LogTab[] = [];
  selectedIndex = 0;
  private subscription: Subscription;

  constructor(public windowService: WindowService,
              private eventBus: EventBusService,
              private service: RuntimeLogService,
              private injector: Injector) {
    this.subscription = this.eventBus.of<LogEvent>(LogEvent.CHANNEL)
      .pipe(map(event => event.log))
      .subscribe(this._onLog.bind(this));
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }

  close(index: number) {
    this.tabs.splice(index, 1);
    if (this.selectedIndex === index) {
      this._updateSelectedIndex(index);
    }
  }

  stop(log: Log) {
    this.service.cancel(log);
  }

  closeFinishedTasks() {
    _.remove(this.tabs, (tab: LogTab) => {
      return tab.lastLog.status === 'CLOSED';
    });
    this._updateSelectedIndex(this.selectedIndex);
  }

  _updateSelectedIndex(index: number) {
    this.selectedIndex = Math.min(Math.max(index, 0), this.tabs.length - 1);
  }

  _onLog(log: Log) {
    const index: number = _.findIndex(this.tabs, commandTab => commandTab.lastLog.id === log.id);
    if (index === -1) {
      this._addLogsTab(log);
      return;
    }
    this.tabs[index].logsSubject.next(log);
    this.tabs[index].lastLog = log;
  }

  _addLogsTab(log: Log) {
    const injectorTokens = new WeakMap();
    const logsSubject = new ReplaySubject<Log>(10);
    injectorTokens.set(RUNTIME_LOGS, logsSubject);
    const content = new ComponentPortal(RuntimeLogsComponent, null, new PortalInjector(this.injector, injectorTokens));
    const command: LogTab = {
      content,
      logsSubject,
      lastLog: log,
    };
    logsSubject.next(log);
    this.tabs.push(command);
    this.selectedIndex = this.tabs.length - 1;
    this.eventBus.publish(new OpenLogsEvent());
  }

}
