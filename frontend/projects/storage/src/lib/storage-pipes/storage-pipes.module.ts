import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {StorageNodeToIconPipe} from 'projects/storage/src/lib/storage-pipes/storage-node-to-icon.pipe';
import {StorageNodeToNamePipe} from 'projects/storage/src/lib/storage-pipes/storage-node-to-name.pipe';
import {StorageNodeToParentPathPipe} from 'projects/storage/src/lib/storage-pipes/storage-node-to-parent-path.pipe';
import { StorageNodeToExtPipe } from './storage-node-to-ext.pipe';
import { NodeEventToNodePipe } from './node-event-to-node.pipe';
import { StorageNodeToPredicatePipe } from './storage-node-to-predicate.pipe';
import {ToolsModule} from 'projects/tools/src/lib/tools.module';

@NgModule({
  declarations: [
    StorageNodeToIconPipe,
    StorageNodeToNamePipe,
    StorageNodeToParentPathPipe,
    StorageNodeToExtPipe,
    NodeEventToNodePipe,
    StorageNodeToPredicatePipe,
  ],
  imports: [
    CommonModule,
    ToolsModule,
  ],
  providers: [
    StorageNodeToIconPipe,
    StorageNodeToNamePipe,
    StorageNodeToParentPathPipe,
    StorageNodeToExtPipe,
    NodeEventToNodePipe,
    StorageNodeToPredicatePipe,
  ],
  exports: [
    StorageNodeToIconPipe,
    StorageNodeToNamePipe,
    StorageNodeToParentPathPipe,
    StorageNodeToExtPipe,
    NodeEventToNodePipe,
    StorageNodeToPredicatePipe,
  ]
})
export class StoragePipesModule {
}
