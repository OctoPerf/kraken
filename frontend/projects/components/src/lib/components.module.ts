import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {VendorsModule} from 'projects/vendors/src/lib/vendors.module';
import {ToolsModule} from 'projects/tools/src/lib/tools.module';
import {EventModule} from 'projects/event/src/lib/event.module';
import {ColorModule} from 'projects/color/src/lib/color.module';
import {FullPageComponent} from 'projects/components/src/lib/full-page/full-page.component';
import {LoadingIconComponent} from 'projects/components/src/lib/loading-icon/loading-icon.component';
import {RouterProgressComponent} from 'projects/components/src/lib/router-progress/router-progress.component';
import {HeaderComponent} from 'projects/components/src/lib/header/header.component';
import {TableOverlayComponent} from 'projects/components/src/lib/table-overlay/table-overlay.component';
import {SpinnerComponent} from 'projects/components/src/lib/spinner/spinner.component';
import {MessageComponent} from 'projects/components/src/lib/message/message.component';
import {IconModule} from 'projects/icon/src/lib/icon.module';
import { LazyIfDirective } from './lazy-if.directive';
import {TableOverlaySelectionComponent} from 'projects/components/src/lib/table-overlay-selection/table-overlay-selection.component';
import {CopyToClipboardDirective} from 'projects/components/src/lib/copy-to-clipboard.directive';
import {RouterProgressModule} from 'projects/components/src/lib/router-progress/router-progress.module';
import { DurationInputComponent } from './duration-input/duration-input.component';

@NgModule({
  imports: [
    CommonModule,
    VendorsModule,
    ToolsModule,
    EventModule,
    ColorModule,
    IconModule,
    RouterProgressModule,
  ],
  declarations: [
    FullPageComponent,
    LoadingIconComponent,
    HeaderComponent,
    TableOverlayComponent,
    TableOverlaySelectionComponent,
    SpinnerComponent,
    MessageComponent,
    LazyIfDirective,
    CopyToClipboardDirective,
    DurationInputComponent,
  ],
  exports: [
    FullPageComponent,
    LoadingIconComponent,
    HeaderComponent,
    TableOverlayComponent,
    TableOverlaySelectionComponent,
    SpinnerComponent,
    MessageComponent,
    LazyIfDirective,
    CopyToClipboardDirective,
    RouterProgressModule,
    DurationInputComponent,
  ],
})
export class ComponentsModule {
}
