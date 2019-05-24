import {NotificationLevel} from 'projects/notification/src/lib/notification-level';

export interface Notification {
  message: string;
  level: NotificationLevel;
  type: string;
}
