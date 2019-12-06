import {EventEmitter, Injectable, OnDestroy} from '@angular/core';
import {Subscription} from 'rxjs';
import {EventBusService} from 'projects/event/src/lib/event-bus.service';
import * as _ from 'lodash';
import {TaskExecutedEvent} from 'projects/runtime/src/lib/events/task-executed-event';
import {Log} from 'projects/runtime/src/lib/entities/log';
import {LogsAttachedEvent} from 'projects/runtime/src/lib/events/logs-attached-event';
import {RuntimeContainerService} from 'projects/runtime/src/lib/runtime-task/runtime-container.service';

@Injectable()
export class RuntimeLogService implements OnDestroy {

  private subscriptions: Subscription[] = [];
  private logLabels: Map<string, { name: string, title: string }> = new Map([]);
  public logLabelsChanged: EventEmitter<void> = new EventEmitter<void>();

  constructor(private eventBus: EventBusService,
              private runtimeContainerService: RuntimeContainerService,
  ) {

    this.subscriptions.push(
      this.eventBus.of<TaskExecutedEvent>(TaskExecutedEvent.CHANNEL)
        .subscribe(event => {
          this.logLabels.set(event.taskId, {
            name: event.context.description,
            title: `${event.context.taskType} task ${event.context.description}`,
          });
          this.logLabelsChanged.emit();
        })
    );

    this.subscriptions.push(
      this.eventBus.of<LogsAttachedEvent>(LogsAttachedEvent.CHANNEL)
        .subscribe(event => {
          this.logLabels.set(event.logsId, {
            name: event.container.label,
            title: `${event.container.name} on ${event.container.hostId}`,
          });
          this.logLabelsChanged.emit();
        })
    );
  }

  ngOnDestroy() {
    _.invokeMap(this.subscriptions, 'unsubscribe');
  }

  public cancel(log: Log): void {
    this.runtimeContainerService.detachLogs(log.id);
  }

  public label(id: string): { name: string, title: string } {
    return this.logLabels.has(id) ? this.logLabels.get(id) : {name: id, title: id};
  }

  public removeLabel(id: string) {
    this.logLabels.delete(id);
  }
}
