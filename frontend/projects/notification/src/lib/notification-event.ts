import {BusEvent} from 'projects/event/src/lib/bus-event';
import {Notification} from 'projects/notification/src/lib/notification';

export class NotificationEvent extends BusEvent {

  public static readonly CHANNEL = 'notification';

  constructor(public notification: Notification) {
    super(NotificationEvent.CHANNEL);
  }
}
