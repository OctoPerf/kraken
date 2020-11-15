import {EventEmitter, Injectable, OnDestroy} from '@angular/core';
import {Observer, Subscription} from 'rxjs';
import {Retry} from 'projects/tools/src/lib/retry';
import {EventBusService} from 'projects/event/src/lib/event-bus.service';
import {RetriesService} from 'projects/tools/src/lib/retries.service';
import {DurationToStringPipe} from 'projects/date/src/lib/duration-to-string.pipe';
import {NotificationEvent} from 'projects/notification/src/lib/notification-event';
import {BaseNotification} from 'projects/notification/src/lib/base-notification';
import {NotificationLevel} from 'projects/notification/src/lib/notification-level';
import {EventSourceService} from 'projects/sse/src/lib/event-source.service';
import {SSEConfigurationService} from 'projects/sse/src/lib/sse-configuration.service';
import {ConfigurationService} from 'projects/commons/src/lib/config/configuration.service';
import {SSEWrapper} from 'projects/sse/src/lib/entities/sse-wrapper';
import {SSEEvent} from 'projects/sse/src/lib/events/sse-event';
import {SecurityService} from 'projects/security/src/lib/security.service';
import {mergeMap} from 'rxjs/operators';
import * as _ from 'lodash';
import {ReloadEventSourceEvent} from 'projects/sse/src/lib/events/reload-event-source-event';

@Injectable()
export class SSEService implements OnDestroy, Observer<SSEWrapper> {

  _subscription: Subscription;
  _reloadSubscription: Subscription;
  _retry: Retry;
  closed = false;

  constructor(private configuration: ConfigurationService,
              private sseConfiguration: SSEConfigurationService,
              private eventBus: EventBusService,
              private eventSourceService: EventSourceService,
              private security: SecurityService,
              retries: RetriesService) {
    this._retry = retries.get();
    this._reloadSubscription = this.eventBus.of(ReloadEventSourceEvent.CHANNEL).subscribe(value => this.watch());
    this.watch();
  }

  ngOnDestroy(): void {
    this._subscription.unsubscribe();
    this._reloadSubscription.unsubscribe();
    this.closed = true;
  }

  watch() {
    if (this._subscription) {
      this._subscription.unsubscribe();
    }
    const watchUrl = this.sseConfiguration.sseApiUrl(`/watch`);
    const channels = this.sseConfiguration.sseChannels();
    const url = `${watchUrl}?channel=${_.join(channels, '&channel=')}`;
    this._subscription = this.security.token.pipe(mergeMap(token => this.eventSourceService.newObservable(url, {
      converter: JSON.parse,
      headers: {
        'ApplicationId': this.configuration.applicationId,
        'ProjectId': this.configuration.projectId,
        'Authorization': `Bearer ${token}`
      }
    }))).subscribe(this);
  }

  next(sseWrapper: SSEWrapper) {
    if (this._retry.isActive()) {
      this._retry.reset();
      this.eventBus.publish(new NotificationEvent(new BaseNotification(
        `Successfully reconnected to server events.`,
        NotificationLevel.INFO)));
    }
    this.eventBus.publish(new SSEEvent(sseWrapper));
  }

  complete() {
    this.error(null);
  }

  error(err: any) {
    const delay = this._retry.getDelay();
    this.eventBus.publish(new NotificationEvent(new BaseNotification(
      `An error occurred while listening for server events. Will reconnect in ${new DurationToStringPipe().transform(delay)}.`,
      NotificationLevel.ERROR)));
    if (this.closed) {
      return;
    }
    setTimeout(() => {
      this.watch();
      this.eventBus.publish(new ReloadEventSourceEvent());
    }, delay);
  }

}
