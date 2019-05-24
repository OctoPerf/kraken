import {Component, HostBinding, Input} from '@angular/core';
import {faExclamationCircle, faExclamationTriangle, faInfoCircle} from '@fortawesome/free-solid-svg-icons';
import {library} from '@fortawesome/fontawesome-svg-core';
import {IconFa} from 'projects/icon/src/lib/icon-fa';

library.add(faExclamationCircle, faExclamationTriangle, faInfoCircle);

export type MessageLevel = 'error' | 'info' | 'warn' | 'loading';

@Component({
  selector: 'lib-message',
  templateUrl: './message.component.html',
  styleUrls: ['./message.component.scss']
})
export class MessageComponent {

  @Input() level: MessageLevel = 'info';
  @HostBinding('class.centered') @Input() centered = false;

  readonly errorIcon = new IconFa(faExclamationTriangle, 'error');
  readonly warnIcon = new IconFa(faExclamationCircle, 'warn');
  readonly infoIcon = new IconFa(faInfoCircle, 'info');

}
