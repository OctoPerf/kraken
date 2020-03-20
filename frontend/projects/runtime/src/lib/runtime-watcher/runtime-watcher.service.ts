import {Injectable, OnDestroy} from '@angular/core';
import {Subscription} from 'rxjs';
import {EventBusService} from 'projects/event/src/lib/event-bus.service';
import {LogEvent} from 'projects/runtime/src/lib/events/log-event';
import {TasksRefreshEvent} from 'projects/runtime/src/lib/events/tasks-refresh-event';
import {SSEEvent} from 'projects/sse/src/lib/events/sse-event';
import {filter, map} from 'rxjs/operators';

@Injectable()
export class RuntimeWatcherService implements OnDestroy {

  _logSubscription: Subscription;
  _tasksSubscription: Subscription;

  constructor(private eventBus: EventBusService) {
    this._logSubscription = this.eventBus.of<SSEEvent>(SSEEvent.CHANNEL)
      .pipe(filter(event => event.wrapper.type === 'LOG'),
        map(event => event.wrapper.value))
      .subscribe(value => eventBus.publish(new LogEvent(value)));
    this._tasksSubscription = this.eventBus.of<SSEEvent>(SSEEvent.CHANNEL)
      .pipe(filter(event => event.wrapper.type === 'TASKS'),
        map(event => event.wrapper.value))
      .subscribe(value => eventBus.publish(new TasksRefreshEvent(value)));
  }

  ngOnDestroy() {
    this._logSubscription.unsubscribe();
    this._tasksSubscription.unsubscribe();
  }

}
