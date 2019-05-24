import {Directive, HostListener, Input} from '@angular/core';
import {HelpPageId} from 'projects/help/src/lib/help-panel/help-pages';
import {EventBusService} from 'projects/event/src/lib/event-bus.service';
import {OpenHelpEvent} from 'projects/help/src/lib/help-panel/open-help-event';

@Directive({
  selector: '[libOpenHelp]'
})
export class OpenHelpDirective {

  @Input('libOpenHelp') page: HelpPageId;

  constructor(private eventBus: EventBusService) {
  }

  @HostListener('click') open() {
    this.eventBus.publish(new OpenHelpEvent(this.page));
  }
}
