import {Component} from '@angular/core';

import {faEye, faQuestionCircle} from '@fortawesome/free-solid-svg-icons';
import {library} from '@fortawesome/fontawesome-svg-core';
import {CollectionViewer, DataSource} from '@angular/cdk/collections';
import {Observable} from 'rxjs';
import {NotificationService} from 'projects/notification/src/lib/notification.service';
import {Notification} from 'projects/notification/src/lib/notification';
import {IconFa} from 'projects/icon/src/lib/icon-fa';
import {faFileAlt} from '@fortawesome/free-solid-svg-icons/faFileAlt';
import {ErrorNotification} from 'projects/notification/src/lib/error-notification';
import {DialogService} from 'projects/dialog/src/lib/dialog.service';
import {faBroom} from '@fortawesome/free-solid-svg-icons/faBroom';

library.add(faEye, faQuestionCircle, faFileAlt, faBroom);

class NotificationsDataSource implements DataSource<Notification> {
  constructor(public notificationsService: NotificationService) {
  }

  connect(collectionViewer: CollectionViewer): Observable<Notification[]> {
    return this.notificationsService.notificationsSubject.asObservable();
  }

  disconnect(collectionViewer: CollectionViewer): void {
  }
}

@Component({
  selector: 'lib-notifications-table',
  templateUrl: './notifications-table.component.html',
  styleUrls: ['./notifications-table.component.scss']
})
export class NotificationsTableComponent {

  readonly highlightIcon = new IconFa(faEye);
  readonly traceIcon = new IconFa(faFileAlt);
  readonly clearIcon = new IconFa(faBroom);
  readonly openHelpIcon = new IconFa(faQuestionCircle);
  readonly displayedColumns: string[] = ['level', 'message', 'highlight', 'help'];

  dataSource: NotificationsDataSource;

  constructor(public notificationsService: NotificationService,
              private dialogs: DialogService) {
    this.dataSource = new NotificationsDataSource(notificationsService);
  }

  openTrace(notification: ErrorNotification) {
    this.dialogs.logs(notification.message, notification.trace);
  }
}
