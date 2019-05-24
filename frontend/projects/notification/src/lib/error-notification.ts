import {NotificationLevel} from 'projects/notification/src/lib/notification-level';
import {Notification} from 'projects/notification/src/lib/notification';

export class ErrorNotification implements Notification {

  public readonly type = 'BaseNotification';

  constructor(
    public message: string,
    public level: NotificationLevel = NotificationLevel.ERROR,
    public trace?: string,
  ) {
  }
}
