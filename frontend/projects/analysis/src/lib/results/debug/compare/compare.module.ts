import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {CompareDialogComponent} from './compare-dialog/compare-dialog.component';
import {VendorsModule} from 'projects/vendors/src/lib/vendors.module';
import {DateModule} from 'projects/date/src/lib/date.module';
import {DebugChunkSelectorComponent} from './debug-chunk-selector/debug-chunk-selector.component';
import {ComponentsModule} from 'projects/components/src/lib/components.module';
import {EditorModule} from 'projects/editor/src/lib/editor.module';
import {DebugPipesModule} from 'projects/analysis/src/lib/results/debug/debug-pipes/debug-pipes.module';
import {HelpModule} from 'projects/help/src/lib/help.module';

@NgModule({
  declarations: [
    CompareDialogComponent,
    DebugChunkSelectorComponent,
  ],
  exports: [
    CompareDialogComponent,
  ],
  entryComponents: [
    CompareDialogComponent,
  ],
  imports: [
    CommonModule,
    VendorsModule,
    DateModule,
    ComponentsModule,
    EditorModule,
    DebugPipesModule,
    HelpModule,
  ]
})
export class CompareModule {
}
