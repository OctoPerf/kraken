import {SelectHelpEvent} from 'projects/help/src/lib/help-panel/select-help-event';

describe('SelectHelpEvent', () => {

  it('should create', () => {
    expect(new SelectHelpEvent('HOME')).toBeTruthy();
    expect(new SelectHelpEvent(null)).toBeTruthy();
  });

});
