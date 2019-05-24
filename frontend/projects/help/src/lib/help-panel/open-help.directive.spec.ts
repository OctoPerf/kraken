import {eventBusSpy} from 'projects/event/src/lib/event-bus.service.spec';
import {OpenHelpDirective} from 'projects/help/src/lib/help-panel/open-help.directive';
import {OpenHelpEvent} from 'projects/help/src/lib/help-panel/open-help-event';

describe('OpenHelpDirective', () => {
  it('should fire event on click', () => {
    const eventBus = eventBusSpy();
    const directive = new OpenHelpDirective(eventBus);
    expect(directive).toBeTruthy();
    directive.page = 'HOME';
    directive.open();
    expect(eventBus.publish).toHaveBeenCalledWith(new OpenHelpEvent('HOME'));
  });
});
