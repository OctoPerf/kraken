import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {VendorsModule} from 'projects/vendors/src/lib/vendors.module';
import {ToolsModule} from 'projects/tools/src/lib/tools.module';
import {EventModule} from 'projects/event/src/lib/event.module';
import {SplitPanesComponent} from 'projects/split/src/lib/split-panes/split-panes.component';
import {SplitPaneDirective} from 'projects/split/src/lib/split-pane.directive';
import {SplitGutterDirective} from 'projects/split/src/lib/split-gutter.directive';

@NgModule({
  imports: [
    CommonModule,
    VendorsModule,
    ToolsModule,
    EventModule,
  ],
  declarations: [
    SplitPanesComponent,
    SplitPaneDirective,
    SplitGutterDirective,
  ],
  exports: [SplitPanesComponent],
})
export class SplitModule { }
