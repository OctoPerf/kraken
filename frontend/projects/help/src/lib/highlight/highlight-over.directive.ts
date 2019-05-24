import {Directive, HostListener, Input, OnInit} from '@angular/core';
import {BusEvent} from 'projects/event/src/lib/bus-event';
import {HighlightService} from 'projects/help/src/lib/highlight/highlight.service';
import {EventBusService} from 'projects/event/src/lib/event-bus.service';

@Directive({
  selector: '[libHighlightOver]'
})
export class HighlightOverDirective implements OnInit {

  @Input('appHighlightOver') selector: string;
  @Input() busEventChannel?: string;
  @Input() busEvent?: BusEvent;
  @Input() duration?: number;
  highlighted = false;
  event: BusEvent;

  constructor(private highlightService: HighlightService,
              private eventBus: EventBusService) {
  }

  ngOnInit() {
    if (this.busEvent) {
      this.event = this.busEvent;
    } else if (this.busEventChannel) {
      this.event = new BusEvent(this.busEventChannel);
    } else {
      this.event = new BusEvent('noop');
    }
  }

  @HostListener('mousemove') highlight() {
    if (this.highlighted) {
      return;
    }
    this.highlighted = true;
    this.eventBus.publish(this.event);
    setTimeout(() => {
      this.highlightService.highlight(this.selector, this.duration);
      setTimeout(() => this.highlighted = false, 3000);
    });
  }

}
