import {Injectable, OnDestroy} from '@angular/core';
import {EventBusService} from 'projects/event/src/lib/event-bus.service';
import {StorageWatcherEvent} from 'projects/storage/src/lib/entities/storage-watcher-event';
import {NodeCreatedEvent} from 'projects/storage/src/lib/events/node-created-event';
import {NodeDeletedEvent} from 'projects/storage/src/lib/events/node-deleted-event';
import {NodeModifiedEvent} from 'projects/storage/src/lib/events/node-modified-event';
import {Subscription} from 'rxjs';
import {SSEEvent} from 'projects/sse/src/lib/events/sse-event';
import {filter, map} from 'rxjs/operators';

@Injectable()
export class StorageWatcherService implements OnDestroy {

  _subscription: Subscription;

  constructor(private eventBus: EventBusService) {
    this._subscription = this.eventBus.of<SSEEvent>(SSEEvent.CHANNEL)
      .pipe(filter(event => event.wrapper.type === 'NODE'),
        map(event => event.wrapper.value as StorageWatcherEvent))
      .subscribe(watcherEvent => {
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
      });
  }

  ngOnDestroy() {
    this._subscription.unsubscribe();
  }
}
