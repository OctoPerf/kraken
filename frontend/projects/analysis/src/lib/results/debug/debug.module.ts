import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {StorageModule} from 'projects/storage/src/lib/storage.module';
import {StoragePipesModule} from 'projects/storage/src/lib/storage-pipes/storage-pipes.module';
import {VendorsModule} from 'projects/vendors/src/lib/vendors.module';
import {DateModule} from 'projects/date/src/lib/date.module';
import {IconModule} from 'projects/icon/src/lib/icon.module';
import {DebugEditorComponent} from 'projects/analysis/src/lib/results/debug/debug-editor/debug-editor.component';
import {StorageNodeEditorsModule} from 'projects/storage/src/lib/storage-editor/storage-node-editors/storage-node-editors.module';
import {EditorModule} from 'projects/editor/src/lib/editor.module';
import {HeadersTableComponent} from 'projects/analysis/src/lib/results/debug/headers-table/headers-table.component';
import {CookiesTableComponent} from 'projects/analysis/src/lib/results/debug/cookies-table/cookies-table.component';
import {SplitModule} from 'projects/split/src/lib/split.module';
import {ComponentsModule} from 'projects/components/src/lib/components.module';
import {TreeModule} from 'projects/tree/src/lib/tree.module';
import {CompareModule} from 'projects/analysis/src/lib/results/debug/compare/compare.module';
import {DebugPipesModule} from 'projects/analysis/src/lib/results/debug/debug-pipes/debug-pipes.module';
import {DebugEntriesTableComponent} from 'projects/analysis/src/lib/results/debug/debug-entries-table/debug-entries-table.component';
import {ToolsModule} from 'projects/tools/src/lib/tools.module';

@NgModule({
  declarations: [
    DebugEntriesTableComponent,
    DebugEditorComponent,
    HeadersTableComponent,
    CookiesTableComponent,
  ],
  exports: [
    DebugEntriesTableComponent,
    DebugEditorComponent,
    CompareModule,
  ],
  entryComponents: [
    DebugEntriesTableComponent,
    DebugEditorComponent,
  ],
  imports: [
    CommonModule,
    VendorsModule,
    IconModule,
    DateModule,
    StorageModule,
    StoragePipesModule,
    StorageNodeEditorsModule,
    EditorModule,
    SplitModule,
    ComponentsModule,
    TreeModule,
    CompareModule,
    DebugPipesModule,
    ToolsModule,
  ],
})
export class DebugModule {
}
