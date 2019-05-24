import {Directive, HostListener, Input, OnInit} from '@angular/core';
import {BusEvent} from 'projects/event/src/lib/bus-event';
import {HighlightService} from 'projects/help/src/lib/highlight/highlight.service';
import {EventBusService} from 'projects/event/src/lib/event-bus.service';

@Directive({
  selector: '[libHighlight]'
})
export class HighlightDirective implements OnInit {

  @Input('libHighlight') selector: string;
  @Input() busEventChannel?: string;
  @Input() busEvent?: BusEvent;
  @Input() duration?: number;
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

  @HostListener('click') highlight() {
    this.eventBus.publish(this.event);
    setTimeout(() => {
      this.highlightService.highlight(this.selector, this.duration);
    });
  }

}
