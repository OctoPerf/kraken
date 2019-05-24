import {HelpModule} from 'projects/help/src/lib/help.module';
import {EventBusService} from 'projects/event/src/lib/event-bus.service';
import {HelpService} from 'projects/help/src/lib/help-panel/help.service';

describe('HelpModule', () => {
  let helpModule: HelpModule;

  beforeEach(() => {
    helpModule = new HelpModule(new HelpService(new EventBusService()));
  });

  it('should create an instance', () => {
    expect(helpModule).toBeTruthy();
    expect(helpModule.helpService).toBeTruthy();
  });
});
