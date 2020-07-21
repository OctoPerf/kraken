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
import {flatMap} from 'rxjs/operators';

@Injectable()
export class SSEService implements OnDestroy, Observer<SSEWrapper> {

  _subscription: Subscription;
  _retry: Retry;
  closed = false;
  public reconnected: EventEmitter<void> = new EventEmitter<void>();

  constructor(private configuration: ConfigurationService,
              private sseConfiguration: SSEConfigurationService,
              private eventBus: EventBusService,
              private eventSourceService: EventSourceService,
              private security: SecurityService,
              retries: RetriesService) {
    this._retry = retries.get();
    this.watch();
  }

  ngOnDestroy(): void {
    this._subscription.unsubscribe();
    this.closed = true;
  }

  watch() {
    if (this._subscription) {
      this._subscription.unsubscribe();
    }
    this._subscription = this.security.token.pipe(flatMap(token => this.eventSourceService.newObservable(this.sseConfiguration.sseApiUrl(`/watch`), {
      converter: JSON.parse,
      headers: {
        'ApplicationId': this.configuration.applicationId,
        'Authorization': `Bearer ${token}`
      }
    }))).subscribe(this);
  }

  next(sseWrapper: SSEWrapper) {
    this._retry.reset();
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
      this.reconnected.emit();
    }, delay);
  }

}
