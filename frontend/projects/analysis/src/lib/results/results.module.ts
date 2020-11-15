import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {ResultsTableComponent} from 'projects/analysis/src/lib/results/results-table/results-table.component';
import {StorageModule} from 'projects/storage/src/lib/storage.module';
import {VendorsModule} from 'projects/vendors/src/lib/vendors.module';
import {DateModule} from 'projects/date/src/lib/date.module';
import {IconModule} from 'projects/icon/src/lib/icon.module';
import {DialogModule} from 'projects/dialog/src/lib/dialog.module';
import {DebugModule} from 'projects/analysis/src/lib/results/debug/debug.module';
import {ResultsTableService} from 'projects/analysis/src/lib/results/results-table/results-table.service';
import {IsDebugEntryStorageNodePipe} from 'projects/analysis/src/lib/results/is-debug-entry-storage-node.pipe';
import {TreeModule} from 'projects/tree/src/lib/tree.module';
import {StorageListService} from 'projects/storage/src/lib/storage-list.service';
import {ComponentsModule} from 'projects/components/src/lib/components.module';
import {StoragePipesModule} from 'projects/storage/src/lib/storage-pipes/storage-pipes.module';
import {ToolsModule} from 'projects/tools/src/lib/tools.module';
import {AnalysisDialogsModule} from 'projects/analysis/src/lib/analysis-dialogs/analysis-dialogs.module';
import {CookieService} from 'ngx-cookie-service';

@NgModule({
  declarations: [ResultsTableComponent, IsDebugEntryStorageNodePipe],
  exports: [ResultsTableComponent, DebugModule, AnalysisDialogsModule],
  imports: [
    CommonModule,
    StorageModule,
    VendorsModule,
    DateModule,
    IconModule,
    DialogModule,
    TreeModule,
    ComponentsModule,
    StoragePipesModule,
    ToolsModule,
    AnalysisDialogsModule,
  ],
  providers: [
    ResultsTableService,
    StorageListService,
    IsDebugEntryStorageNodePipe,
    CookieService,
  ]
})
export class ResultsModule {
  constructor(results: ResultsTableService) {
    results.init();
  }
}
