import {Directive, HostListener, Input} from '@angular/core';
import {GuiToolsService} from 'projects/tools/src/lib/gui-tools.service';
import {EventBusService} from 'projects/event/src/lib/event-bus.service';
import {NotificationEvent} from 'projects/notification/src/lib/notification-event';
import {BaseNotification} from 'projects/notification/src/lib/base-notification';
import {NotificationLevel} from 'projects/notification/src/lib/notification-level';

@Directive({
  selector: '[libCopyToClipboard]'
})
export class CopyToClipboardDirective {

  @Input('libCopyToClipboard') text: string;

  constructor(private guiTools: GuiToolsService,
              private events: EventBusService) {
  }

  @HostListener('click') onClick() {
    this.guiTools.copyToClipboard(this.text);
    this.events.publish(new NotificationEvent(new BaseNotification(`Text copied to clipboard.`,
      NotificationLevel.INFO)));
  }

}
