import {EventEmitter, Injectable, OnDestroy} from '@angular/core';
import {Subscription} from 'rxjs';
import {EventBusService} from 'projects/event/src/lib/event-bus.service';
import * as _ from 'lodash';
import {TaskExecutedEvent} from 'projects/runtime/src/lib/events/task-executed-event';
import {Log} from 'projects/runtime/src/lib/entities/log';
import {LogsAttachedEvent} from 'projects/runtime/src/lib/events/logs-attached-event';
import {RuntimeContainerService} from 'projects/runtime/src/lib/runtime-task/runtime-container.service';
import {RuntimeTaskService} from 'projects/runtime/src/lib/runtime-task/runtime-task.service';
import {flatMap} from 'rxjs/operators';
import {DialogService} from 'projects/dialog/src/lib/dialog.service';
import {NotificationEvent} from 'projects/notification/src/lib/notification-event';
import {BaseNotification} from 'projects/notification/src/lib/base-notification';
import {NotificationLevel} from 'projects/notification/src/lib/notification-level';

@Injectable()
export class RuntimeLogService implements OnDestroy {

  private subscriptions: Subscription[] = [];
  private logLabels: Map<string, { name: string, title: string }> = new Map([]);
  public logLabelsChanged: EventEmitter<void> = new EventEmitter<void>();

  constructor(private eventBus: EventBusService,
              private runtimeContainerService: RuntimeContainerService,
              private runtimeTaskService: RuntimeTaskService,
              private dialogs: DialogService,
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
    switch (log.type) {
      case 'TASK':
        const tasks = this.runtimeTaskService.tasksSubject.getValue();
        const task = _.find(tasks, {id: log.id});
        if (task) {
          this.dialogs.confirm('Cancel Task', 'Are you sure you want to cancel this task execution?')
            .pipe(flatMap(() => this.runtimeTaskService.cancel(task.id, task.type))).subscribe();
        } else {
          this.eventBus.publish(new NotificationEvent(
            new BaseNotification(
              `Cannot find task with id ${log.id}.`,
              NotificationLevel.ERROR,
            )));
        }
        break;
      case 'CONTAINER':
        this.runtimeContainerService.detachLogs(log.id);
        break;
    }
  }

  public label(id: string): { name: string, title: string } {
    return this.logLabels.has(id) ? this.logLabels.get(id) : {name: id, title: id};
  }

  public removeLabel(id: string) {
    this.logLabels.delete(id);
  }
}
