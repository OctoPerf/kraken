import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {VendorsModule} from 'projects/vendors/src/lib/vendors.module';
import {EventModule} from 'projects/event/src/lib/event.module';
import {HighlightModule} from 'projects/help/src/lib/highlight/highlight.module';
import {HelpPanelComponent} from 'projects/help/src/lib/help-panel/help-panel.component';
import {InlineHelpComponent} from 'projects/help/src/lib/inline-help/inline-help.component';
import {OpenHelpDirective} from 'projects/help/src/lib/help-panel/open-help.directive';
import {HelpService} from 'projects/help/src/lib/help-panel/help.service';
import {IconModule} from 'projects/icon/src/lib/icon.module';
import { HelpAnchorComponent } from './help-anchor/help-anchor.component';
import {OpenHelpExtDirective} from 'projects/help/src/lib/help-panel/open-help-ext.directive';

@NgModule({
  imports: [
    CommonModule,
    VendorsModule,
    IconModule,
    EventModule,
    HighlightModule,
  ],
  declarations: [
    HelpPanelComponent,
    InlineHelpComponent,
    OpenHelpDirective,
    OpenHelpExtDirective,
    HelpAnchorComponent,
  ],
  exports: [
    HelpPanelComponent,
    InlineHelpComponent,
    HighlightModule,
    OpenHelpDirective,
    OpenHelpExtDirective,
    HelpAnchorComponent,
  ],
})
export class HelpModule {
  constructor(public helpService: HelpService) {
    // inject helpService to force initialization
  }
}
