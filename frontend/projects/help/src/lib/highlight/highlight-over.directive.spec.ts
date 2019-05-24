import {fakeAsync, tick} from '@angular/core/testing';
import {HighlightService} from 'projects/help/src/lib/highlight/highlight.service';
import {EventBusService} from 'projects/event/src/lib/event-bus.service';
import {HighlightOverDirective} from 'projects/help/src/lib/highlight/highlight-over.directive';
import {highlightServiceSpy} from 'projects/help/src/lib/highlight/highlight.service.spec';
import {eventBusSpy} from 'projects/event/src/lib/event-bus.service.spec';
import {BusEvent} from 'projects/event/src/lib/bus-event';
import {OpenHelpEvent} from 'projects/help/src/lib/help-panel/open-help-event';

describe('HighlightOverDirective', () => {
  let highlightService: HighlightService;
  let eventBus: EventBusService;
  let directive: HighlightOverDirective;

  beforeEach(() => {
    highlightService = highlightServiceSpy();
    eventBus = eventBusSpy();
    directive = new HighlightOverDirective(highlightService, eventBus);
    directive.selector = 'selector';
    directive.duration = 1337;
  });

  it('should create an instance', () => {
    expect(directive).toBeTruthy();
  });

  it('should highlight', fakeAsync(() => {
    directive.highlight();
    expect(directive.highlighted).toBe(true);
    expect(eventBus.publish).toHaveBeenCalledWith(directive.event);
    expect(highlightService.highlight).not.toHaveBeenCalled();
    tick(1);
    expect(highlightService.highlight).toHaveBeenCalledWith(directive.selector, 1337);
    tick(3001);
    expect(directive.highlighted).toBe(false);
  }));

  it('should not highlight', () => {
    directive.highlighted = true;
    directive.highlight();
    expect(eventBus.publish).not.toHaveBeenCalled();
  });

  it('should init event default', () => {
    directive.ngOnInit();
    expect(directive.event).toEqual(new BusEvent('noop'));
  });

  it('should init event channel', () => {
    directive.busEventChannel = 'test';
    directive.ngOnInit();
    expect(directive.event).toEqual(new BusEvent('test'));
  });

  it('should init event', () => {
    directive.busEvent = new OpenHelpEvent('HOME');
    directive.ngOnInit();
    expect(directive.event).toBe(directive.busEvent);
  });
});
