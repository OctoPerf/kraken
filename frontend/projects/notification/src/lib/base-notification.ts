import {HelpPageId} from 'projects/help/src/lib/help-panel/help-pages';
import {BusEvent} from 'projects/event/src/lib/bus-event';
import {NotificationLevel} from 'projects/notification/src/lib/notification-level';
import {Notification} from 'projects/notification/src/lib/notification';

export class BaseNotification implements Notification {

  public readonly type = 'BaseNotification';

  constructor(
    public message: string,
    public level: NotificationLevel = NotificationLevel.INFO,
    public helpPage?: HelpPageId,
    public highlight?: {
      selector: string,
      busEvent: BusEvent,
    }
  ) {
  }
}
