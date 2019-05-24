import {EventEmitter, Injectable, OnDestroy} from '@angular/core';
import {EventBusService} from 'projects/event/src/lib/event-bus.service';
import {NotificationEvent} from 'projects/notification/src/lib/notification-event';
import {NotificationLevel} from 'projects/notification/src/lib/notification-level';
import {StorageWatcherEvent} from 'projects/storage/src/lib/entities/storage-watcher-event';
import {NodeCreatedEvent} from 'projects/storage/src/lib/events/node-created-event';
import {NodeDeletedEvent} from 'projects/storage/src/lib/events/node-deleted-event';
import {NodeModifiedEvent} from 'projects/storage/src/lib/events/node-modified-event';
import {EventSourceService} from 'projects/tools/src/lib/event-source.service';
import {BaseNotification} from 'projects/notification/src/lib/base-notification';
import {Subscription} from 'rxjs';
import {StorageConfigurationService} from 'projects/storage/src/lib/storage-configuration.service';
import {StorageService} from 'projects/storage/src/lib/storage.service';
import {DurationToStringPipe} from 'projects/date/src/lib/duration-to-string.pipe';
import {RetriesService} from 'projects/tools/src/lib/retries.service';
import {OpenStorageTreeEvent} from 'projects/storage/src/lib/events/open-storage-tree-event';
import {Retry} from 'projects/tools/src/lib/retry';

@Injectable()
export class StorageWatcherService implements OnDestroy {

  _subscription: Subscription;
  _retry: Retry;
  _destroyed = false;
  public reconnected: EventEmitter<void> = new EventEmitter<void>();

  constructor(private configuration: StorageConfigurationService,
              private eventBus: EventBusService,
              private eventSourceService: EventSourceService,
              retries: RetriesService,
              private durationToString: DurationToStringPipe) {
    this._retry = retries.get();
    this.watch();
  }

  ngOnDestroy(): void {
    this._subscription.unsubscribe();
    this._destroyed = true;
  }

  watch() {
    if (this._subscription) {
      this._subscription.unsubscribe();
    }
    this._subscription = this.eventSourceService.newObservable(this.configuration.storageApiUrl('/watch'), {converter: JSON.parse})
      .subscribe(this._onMessage.bind(this), this._onError.bind(this), this._onError.bind(this));
  }

  _onMessage(watcherEvent: StorageWatcherEvent) {
    this._retry.reset();
    switch (watcherEvent.event) {
      case 'CREATE':
        this.eventBus.publish(new NodeCreatedEvent(watcherEvent.node));
        break;
      case 'DELETE':
        this.eventBus.publish(new NodeDeletedEvent(watcherEvent.node));
        break;
      case 'MODIFY':
        this.eventBus.publish(new NodeModifiedEvent(watcherEvent.node));
        break;
    }
  }

  _onError() {
    const delay = this._retry.getDelay();
    this.eventBus.publish(new NotificationEvent(new BaseNotification(
      `An error occurred while listening for file events. Will reconnect in ${this.durationToString.transform(delay)}.`,
      NotificationLevel.ERROR,
      null,
      {
        selector: 'lib-storage-tree',
        busEvent: new OpenStorageTreeEvent()
      })));

    if (this._destroyed) {
      return;
    }
    setTimeout(() => {
      this.watch();
      this.reconnected.emit();
    }, delay);
  }
}
