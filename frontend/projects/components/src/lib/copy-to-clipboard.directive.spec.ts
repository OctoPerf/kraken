import {CopyToClipboardDirective} from './copy-to-clipboard.directive';
import {GuiToolsService} from 'projects/tools/src/lib/gui-tools.service';
import {EventBusService} from 'projects/event/src/lib/event-bus.service';
import {guiToolsServiceSpy} from 'projects/tools/src/lib/gui-tools.service.spec';
import {eventBusSpy} from 'projects/event/src/lib/event-bus.service.spec';
import SpyObj = jasmine.SpyObj;

describe('CopyToClipboardDirective', () => {

  let directive: CopyToClipboardDirective;
  let guiTools: SpyObj<GuiToolsService>;
  let events: SpyObj<EventBusService>;

  beforeEach(() => {
    guiTools = guiToolsServiceSpy();
    events = eventBusSpy();
    directive = new CopyToClipboardDirective(guiTools, events);
  });

  it('should create an instance', () => {
    expect(directive).toBeTruthy();
  });

  it('should copy to clipboard', () => {
    directive.onClick();
    expect(guiTools.copyToClipboard).toHaveBeenCalled();
    expect(events.publish).toHaveBeenCalled();
  });
});
