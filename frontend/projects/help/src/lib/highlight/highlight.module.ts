import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {VendorsModule} from 'projects/vendors/src/lib/vendors.module';
import {HighlightBackdropComponent} from 'projects/help/src/lib/highlight/highlight-backdrop/highlight-backdrop.component';
import {HighlightDirective} from 'projects/help/src/lib/highlight/highlight.directive';
import {HighlightOverDirective} from 'projects/help/src/lib/highlight/highlight-over.directive';

@NgModule({
  imports: [
    CommonModule,
    VendorsModule,
  ],
  declarations: [
    HighlightBackdropComponent,
    HighlightDirective,
    HighlightOverDirective,
  ],
  entryComponents: [
    HighlightBackdropComponent
  ],
  exports: [
    HighlightDirective,
    HighlightOverDirective,
  ]
})
export class HighlightModule {
}
