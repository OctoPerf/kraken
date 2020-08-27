import {OpenHelpExtDirective} from 'projects/help/src/lib/help-panel/open-help-ext.directive';
import {windowServiceSpy} from 'projects/tools/src/lib/window.service.spec';
import {configurationServiceMock} from 'projects/commons/src/lib/config/configuration.service.spec';
import {helpServiceSpy} from 'projects/help/src/lib/help-panel/help.service.spec';

describe('OpenHelpExtDirective', () => {
  it('should fire event on click', () => {
    const window = windowServiceSpy();
    const configuration = configurationServiceMock();
    const helpService = helpServiceSpy();
    const directive = new OpenHelpExtDirective(window, configuration, helpService);
    expect(directive).toBeTruthy();
    directive.page = 'HOME';
    directive.open();
    expect(window.open).toHaveBeenCalled();
  });
});
