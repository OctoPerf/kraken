import {Injectable, OnDestroy} from '@angular/core';
import {BehaviorSubject, Subscription} from 'rxjs';
import { MatSnackBar } from '@angular/material/snack-bar';
import {EventBusService} from 'projects/event/src/lib/event-bus.service';
import {TabsService} from 'projects/tabs/src/lib/tabs.service';
import {HighlightService} from 'projects/help/src/lib/highlight/highlight.service';
import {NotificationEvent} from 'projects/notification/src/lib/notification-event';
import {Notification} from 'projects/notification/src/lib/notification';
import * as _ from 'lodash';
import {OpenNotificationsEvent} from 'projects/notification/src/lib/open-notifications-event';

@Injectable()
export class NotificationService implements OnDestroy {

  private subscriptions: Subscription[] = [];
  notifications: Notification[];
  public notificationsSubject: BehaviorSubject<Notification[]>;

  constructor(private eventBus: EventBusService,
              private snackBar: MatSnackBar,
              private tabsService: TabsService,
              private highlight: HighlightService) {
    this.notifications = [];
    this.notificationsSubject = new BehaviorSubject<Notification[]>(this.notifications);
    this.subscriptions.push(this.eventBus.of<NotificationEvent>(NotificationEvent.CHANNEL).subscribe(event => {
      this.notifications.splice(0, 0, event.notification);
      this.notificationsSubject.next(this.notifications);
      this.subscriptions.push(this.snackBar.open(event.notification.message, 'Open', {
        panelClass: 'snackbar-long-text'
      })
        .onAction().subscribe(this._snackbarAction.bind(this)));
    }));
  }

  ngOnDestroy() {
    _.invokeMap(this.subscriptions, 'unsubscribe');
  }

  clear() {
    this.notifications = [];
    this.notificationsSubject.next([]);
  }

  get count(): number {
    return this.notifications.length;
  }

  _snackbarAction() {
    if (this.tabsService.isSelected({label: 'Notifications'})) {
      this.highlight.highlight('lib-notification-table');
    } else {
      this.eventBus.publish(new OpenNotificationsEvent());
    }
  }

}
