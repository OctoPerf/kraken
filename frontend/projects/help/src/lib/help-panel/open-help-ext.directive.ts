import {Directive, HostListener, Input} from '@angular/core';
import {HelpPageId} from 'projects/help/src/lib/help-panel/help-page-id';
import {WindowService} from 'projects/tools/src/lib/window.service';
import {of} from 'rxjs';
import {HELP_PAGES} from 'projects/help/src/lib/help-panel/help-pages';
import {ConfigurationService} from 'projects/commons/src/lib/config/configuration.service';
import {HelpService} from 'projects/help/src/lib/help-panel/help.service';

@Directive({
  selector: '[libOpenHelpExt]'
})
export class OpenHelpExtDirective {

  @Input('libOpenHelpExt') page: HelpPageId;

  constructor(private window: WindowService,
              private configuration: ConfigurationService,
              private helpService: HelpService) {
  }

  @HostListener('click') open() {
    const id = this.page || this.helpService.lastPage.getValue();
    const url = this.configuration.docUrl(HELP_PAGES[id]);
    this.window.open(of(url));
  }
}
