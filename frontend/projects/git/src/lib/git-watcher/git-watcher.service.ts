import {Injectable, OnDestroy} from '@angular/core';
import {Subscription} from 'rxjs';
import {EventBusService} from 'projects/event/src/lib/event-bus.service';
import {SSEEvent} from 'projects/sse/src/lib/events/sse-event';
import {filter, map} from 'rxjs/operators';
import {GitStatusEvent} from 'projects/git/src/lib/events/git-status-event';
import {GitLogEvent} from 'projects/git/src/lib/events/git-log-event';
import {GitRefreshStorageEvent} from 'projects/git/src/lib/events/git-refresh-storage-event';

@Injectable()
export class GitWatcherService implements OnDestroy {

  _statusSubscription: Subscription;
  _logsSubscription: Subscription;
  _refreshSubscription: Subscription;

  constructor(private eventBus: EventBusService) {
    this._statusSubscription = this.eventBus.of<SSEEvent>(SSEEvent.CHANNEL)
      .pipe(filter(event => event.wrapper.type === 'GIT_STATUS'),
        map(event => event.wrapper.value))
      .subscribe(value => eventBus.publish(new GitStatusEvent(value)));

    this._logsSubscription = this.eventBus.of<SSEEvent>(SSEEvent.CHANNEL)
      .pipe(filter(event => event.wrapper.type === 'GIT_LOG'),
        map(event => event.wrapper.value))
      .subscribe(value => eventBus.publish(new GitLogEvent(value.text)));

    this._refreshSubscription = this.eventBus.of<SSEEvent>(SSEEvent.CHANNEL)
      .pipe(filter(event => event.wrapper.type === 'GIT_REFRESH'))
      .subscribe(value => eventBus.publish(new GitRefreshStorageEvent()));
  }

  ngOnDestroy() {
    this._statusSubscription.unsubscribe();
    this._logsSubscription.unsubscribe();
    this._refreshSubscription.unsubscribe();
  }
}
