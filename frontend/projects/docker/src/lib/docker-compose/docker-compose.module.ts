import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {DockerComposeEditorComponent} from './docker-compose-editor/docker-compose-editor.component';
import {EditorModule} from 'projects/editor/src/lib/editor.module';
import {StorageNodeEditorsModule} from 'projects/storage/src/lib/storage-editor/storage-node-editors/storage-node-editors.module';
import {VendorsModule} from 'projects/vendors/src/lib/vendors.module';
import {IconModule} from 'projects/icon/src/lib/icon.module';
import {StoragePipesModule} from 'projects/storage/src/lib/storage-pipes/storage-pipes.module';
import {ToolsModule} from 'projects/tools/src/lib/tools.module';
import {CommandModule} from 'projects/command/src/lib/command.module';

@NgModule({
  imports: [
    CommonModule,
    EditorModule,
    StorageNodeEditorsModule,
    StoragePipesModule,
    VendorsModule,
    IconModule,
    ToolsModule,
    CommandModule,
  ],
  providers: [
  ],
  declarations: [
    DockerComposeEditorComponent,
  ],
  exports: [
    DockerComposeEditorComponent,
  ],
  entryComponents: [
    DockerComposeEditorComponent,
  ]
})
export class DockerComposeModule {
}
