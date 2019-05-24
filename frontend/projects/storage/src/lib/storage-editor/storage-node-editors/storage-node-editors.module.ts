import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {DefaultStorageNodeEditorComponent} from './default-storage-node-editor/default-storage-node-editor.component';
import {MarkdownStorageNodeEditorComponent} from './markdown-storage-node-editor/markdown-storage-node-editor.component';
import {EditorModule} from 'projects/editor/src/lib/editor.module';
import {StoragePipesModule} from 'projects/storage/src/lib/storage-pipes/storage-pipes.module';
import {ComponentsModule} from 'projects/components/src/lib/components.module';
import {ToolsModule} from 'projects/tools/src/lib/tools.module';
import {VendorsModule} from 'projects/vendors/src/lib/vendors.module';
import { EditorMessagesComponent } from './editor-messages/editor-messages.component';

@NgModule({
  declarations: [
    DefaultStorageNodeEditorComponent,
    MarkdownStorageNodeEditorComponent,
    EditorMessagesComponent,
  ],
  imports: [
    CommonModule,
    EditorModule,
    StoragePipesModule,
    ComponentsModule,
    ToolsModule,
    VendorsModule,
  ],
  entryComponents: [
    DefaultStorageNodeEditorComponent,
    MarkdownStorageNodeEditorComponent,
  ],
  exports: [
    EditorMessagesComponent,
  ]
})
export class StorageNodeEditorsModule {
}
