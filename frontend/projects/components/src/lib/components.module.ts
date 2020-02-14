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

@NgModule({
  imports: [
    CommonModule,
    VendorsModule,
    ToolsModule,
    EventModule,
    ColorModule,
    IconModule,
  ],
  declarations: [
    FullPageComponent,
    LoadingIconComponent,
    RouterProgressComponent,
    HeaderComponent,
    TableOverlayComponent,
    TableOverlaySelectionComponent,
    SpinnerComponent,
    MessageComponent,
    LazyIfDirective,
  ],
  exports: [
    FullPageComponent,
    LoadingIconComponent,
    RouterProgressComponent,
    HeaderComponent,
    TableOverlayComponent,
    TableOverlaySelectionComponent,
    SpinnerComponent,
    MessageComponent,
    LazyIfDirective,
  ],
  entryComponents: [],
})
export class ComponentsModule {
}
