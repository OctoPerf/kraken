import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {StorageEditorComponent} from './storage-editor/storage-editor.component';
import {VendorsModule} from 'projects/vendors/src/lib/vendors.module';
import {IconModule} from 'projects/icon/src/lib/icon.module';
import {StoragePipesModule} from 'projects/storage/src/lib/storage-pipes/storage-pipes.module';
import {StorageNodeTabHeaderComponent} from './storage-node-tab-header/storage-node-tab-header.component';
import {StorageEditorService} from 'projects/storage/src/lib/storage-editor/storage-editor.service';
import {StorageNodeEditorsModule} from 'projects/storage/src/lib/storage-editor/storage-node-editors/storage-node-editors.module';
import {ComponentsModule} from 'projects/components/src/lib/components.module';
import {TreeModule} from 'projects/tree/src/lib/tree.module';

@NgModule({
  declarations: [StorageEditorComponent, StorageNodeTabHeaderComponent],
  imports: [
    CommonModule,
    VendorsModule,
    IconModule,
    ComponentsModule,
    StoragePipesModule,
    StorageNodeEditorsModule,
    TreeModule,
  ],
  exports: [
    StorageEditorComponent,
  ],
  entryComponents: [
    StorageEditorComponent,
  ],
  providers: [
    StorageEditorService,
  ]
})
export class StorageEditorModule {
}
