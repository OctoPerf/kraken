import {OpenHelpEvent} from 'projects/help/src/lib/help-panel/open-help-event';

describe('OpenHelpEvent', () => {

  it('should create', () => {
    expect(new OpenHelpEvent('HOME')).toBeTruthy();
    expect(new OpenHelpEvent(null)).toBeTruthy();
  });

});
