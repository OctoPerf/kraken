import {Pipe, PipeTransform} from '@angular/core';
import {NotificationLevel} from 'projects/notification/src/lib/notification-level';
import {Icon} from 'projects/icon/src/lib/icon';
import {IconFa} from 'projects/icon/src/lib/icon-fa';
import {faInfoCircle} from '@fortawesome/free-solid-svg-icons/faInfoCircle';
import {faExclamationCircle} from '@fortawesome/free-solid-svg-icons/faExclamationCircle';
import {faExclamationTriangle} from '@fortawesome/free-solid-svg-icons/faExclamationTriangle';
import {library} from '@fortawesome/fontawesome-svg-core';

library.add(faExclamationCircle, faExclamationTriangle, faInfoCircle);

@Pipe({
  name: 'notificationLevelToIcon'
})
export class NotificationLevelToIconPipe implements PipeTransform {

  private static readonly LEVEL_ICONS: { [key in NotificationLevel]: Icon } = {
    'INFO': new IconFa(faInfoCircle, 'info'),
    'WARNING': new IconFa(faExclamationCircle, 'warn'),
    'ERROR': new IconFa(faExclamationTriangle, 'error'),
  };

  transform(level: NotificationLevel): Icon {
    return NotificationLevelToIconPipe.LEVEL_ICONS[level];
  }

}
