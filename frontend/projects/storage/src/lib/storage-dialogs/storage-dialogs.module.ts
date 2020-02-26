import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FileNameDialogComponent} from 'projects/storage/src/lib/storage-dialogs/file-name-dialog/file-name-dialog.component';
import {DeleteFilesDialogComponent} from 'projects/storage/src/lib/storage-dialogs/delete-files-dialog/delete-files-dialog.component';
import {FileUploadDialogComponent} from 'projects/storage/src/lib/storage-dialogs/file-upload-dialog/file-upload-dialog.component';
import {StoragePipesModule} from 'projects/storage/src/lib/storage-pipes/storage-pipes.module';
import {VendorsModule} from 'projects/vendors/src/lib/vendors.module';
import {ComponentsModule} from 'projects/components/src/lib/components.module';
import {IconModule} from 'projects/icon/src/lib/icon.module';
import {HelpModule} from 'projects/help/src/lib/help.module';

@NgModule({
  declarations: [
    FileNameDialogComponent,
    DeleteFilesDialogComponent,
    FileUploadDialogComponent,
  ],
  imports: [
    StoragePipesModule,
    CommonModule,
    VendorsModule,
    ComponentsModule,
    IconModule,
    HelpModule,
  ],
  exports: [
    FileNameDialogComponent,
    DeleteFilesDialogComponent,
    FileUploadDialogComponent,
  ],
})
export class StorageDialogsModule {
}
