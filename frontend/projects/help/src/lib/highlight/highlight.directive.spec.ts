import {HighlightDirective} from './highlight.directive';
import {HighlightService} from './highlight.service';
import {highlightServiceSpy} from './highlight.service.spec';
import {fakeAsync, tick} from '@angular/core/testing';
import {EventBusService} from 'projects/event/src/lib/event-bus.service';
import {eventBusSpy} from 'projects/event/src/lib/event-bus.service.spec';
import {BusEvent} from 'projects/event/src/lib/bus-event';
import {OpenHelpEvent} from 'projects/help/src/lib/help-panel/open-help-event';

describe('HighlightDirective', () => {
  let highlightService: HighlightService;
  let eventBus: EventBusService;
  let directive: HighlightDirective;

  beforeEach(() => {
    highlightService = highlightServiceSpy();
    eventBus = eventBusSpy();
    directive = new HighlightDirective(highlightService, eventBus);
    directive.selector = 'selector';
    directive.duration = 1337;
  });

  it('should create an instance', () => {
    expect(directive).toBeTruthy();
  });

  it('should highlight', fakeAsync(() => {
    directive.highlight();
    expect(eventBus.publish).toHaveBeenCalledWith(directive.event);
    expect(highlightService.highlight).not.toHaveBeenCalled();
    tick(1);
    expect(highlightService.highlight).toHaveBeenCalledWith(directive.selector, 1337);
  }));

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
