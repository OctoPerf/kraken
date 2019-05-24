import {StorageNode} from 'projects/storage/src/lib/entities/storage-node';
import {InjectionToken} from '@angular/core';
import {StorageNodeEditorContentService} from 'projects/storage/src/lib/storage-editor/storage-node-editors/storage-node-editor-content.service';

export const STORAGE_NODE = new InjectionToken<StorageNode>('StorageNode');

export interface StorageNodeEditor {
  node: StorageNode;
  contentService: StorageNodeEditorContentService;
}
