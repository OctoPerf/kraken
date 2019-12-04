import {Injectable, OnDestroy} from '@angular/core';
import {Observer, Subscription} from 'rxjs';
import {Retry} from 'projects/tools/src/lib/retry';
import {EventBusService} from 'projects/event/src/lib/event-bus.service';
import {EventSourceService} from 'projects/tools/src/lib/event-source.service';
import {ConfigurationService} from 'projects/commons/src/lib/config/configuration.service';
import {HttpClient} from '@angular/common/http';
import {RetriesService} from 'projects/tools/src/lib/retries.service';
import {DurationToStringPipe} from 'projects/date/src/lib/duration-to-string.pipe';
import {NotificationEvent} from 'projects/notification/src/lib/notification-event';
import {BaseNotification} from 'projects/notification/src/lib/base-notification';
import {NotificationLevel} from 'projects/notification/src/lib/notification-level';
import {RuntimeConfigurationService} from 'projects/runtime/src/lib/runtime-configuration.service';
import {SSEWrapper} from 'projects/tools/src/lib/sse-wrapper';
import {LogEvent} from 'projects/runtime/src/lib/events/log-event';
import {TasksRefreshEvent} from 'projects/runtime/src/lib/events/tasks-refresh-event';

@Injectable()
export class RuntimeWatcherService implements OnDestroy, Observer<SSEWrapper> {

  private sseUnwrappers = {
    LOG: (sseWrapper: SSEWrapper) => new LogEvent(sseWrapper.value),
    TASKS: (sseWrapper: SSEWrapper) => new TasksRefreshEvent(sseWrapper.value),
  };
  _eventSourceSubscription: Subscription;
  _retry: Retry;
  closed = false;

  constructor(private eventBus: EventBusService,
              private eventSourceService: EventSourceService,
              private configuration: ConfigurationService,
              private runtimeConfiguration: RuntimeConfigurationService,
              private http: HttpClient,
              retries: RetriesService,
              private durationToString: DurationToStringPipe) {
    this._retry = retries.get();
    this.watch();
  }

  ngOnDestroy() {
    if (this._eventSourceSubscription) {
      this._eventSourceSubscription.unsubscribe();
    }
    this.closed = true;
  }

  watch() {
    if (this._eventSourceSubscription) {
      this._eventSourceSubscription.unsubscribe();
    }
    // ApplicationId Header cannot be used here as it is not supported by SSE
    this._eventSourceSubscription = this.eventSourceService
      .newObservable(this.runtimeConfiguration.runtimeApiUrl(`/watch/${this.configuration.applicationId}`), {converter: JSON.parse})
      .subscribe(this);
  }

  next(sseWrapper: SSEWrapper) {
    this._retry.reset();
    this.eventBus.publish(this.sseUnwrappers[sseWrapper.type](sseWrapper));
  }

  error(err: any) {
    const delay = this._retry.getDelay();
    this.eventBus.publish(new NotificationEvent(
      new BaseNotification(
        `An error occurred while listening for runtime events. Will reconnect in ${this.durationToString.transform(delay)}.`,
        NotificationLevel.ERROR)));
    if (!this.closed) {
      setTimeout(this.watch.bind(this), delay);
    }
  }

  complete() {
    this.error(null);
  }

}
