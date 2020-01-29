import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {StorageNodeComponent} from 'projects/storage/src/lib/storage-tree/storage-node/storage-node.component';
import {StorageTreeComponent} from 'projects/storage/src/lib/storage-tree/storage-tree/storage-tree.component';
import {VendorsModule} from 'projects/vendors/src/lib/vendors.module';
import {IconModule} from 'projects/icon/src/lib/icon.module';
import {TreeModule} from 'projects/tree/src/lib/tree.module';
import {DialogModule} from 'projects/dialog/src/lib/dialog.module';
import {ComponentsModule} from 'projects/components/src/lib/components.module';
import {StoragePipesModule} from 'projects/storage/src/lib/storage-pipes/storage-pipes.module';
import {LinkSelectionButtonComponent} from './link-selection-button/link-selection-button.component';
import {DateModule} from 'projects/date/src/lib/date.module';
import {ToolsModule} from 'projects/tools/src/lib/tools.module';
import {FileNameDialogComponent} from 'projects/storage/src/lib/storage-dialogs/file-name-dialog/file-name-dialog.component';
import {DeleteFilesDialogComponent} from 'projects/storage/src/lib/storage-dialogs/delete-files-dialog/delete-files-dialog.component';
import {FileUploadDialogComponent} from 'projects/storage/src/lib/storage-dialogs/file-upload-dialog/file-upload-dialog.component';
import {StorageMenuModule} from 'projects/storage/src/lib/storage-menu/storage-menu.module';

@NgModule({
  declarations: [
    StorageNodeComponent,
    StorageTreeComponent,
    LinkSelectionButtonComponent,
  ],
  imports: [
    CommonModule,
    VendorsModule,
    IconModule,
    TreeModule,
    DialogModule,
    ComponentsModule,
    StoragePipesModule,
    StorageMenuModule,
    DateModule,
    ToolsModule,
  ],
  exports: [
    StorageTreeComponent,
  ],
  entryComponents: [
    StorageTreeComponent,
    FileNameDialogComponent,
    DeleteFilesDialogComponent,
    FileUploadDialogComponent,
  ]
})
export class StorageTreeModule {
}
