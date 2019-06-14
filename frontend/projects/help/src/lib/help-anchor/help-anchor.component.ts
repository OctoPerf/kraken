import {Component, Input, OnInit} from '@angular/core';
import {HelpPageId} from 'projects/help/src/lib/help-panel/help-pages';
import {EventBusService} from 'projects/event/src/lib/event-bus.service';
import {SelectHelpEvent} from 'projects/help/src/lib/help-panel/select-help-event';

@Component({
  selector: 'lib-help-anchor',
  template: ''
})
export class HelpAnchorComponent implements OnInit {

  @Input() pageId: HelpPageId;

  constructor(private eventBus: EventBusService) {
  }

  ngOnInit() {
    if (this.pageId) {
      this.eventBus.publish(new SelectHelpEvent(this.pageId));
    }
  }

}
