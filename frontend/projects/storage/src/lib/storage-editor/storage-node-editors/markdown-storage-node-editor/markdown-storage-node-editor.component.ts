import {Component, Inject} from '@angular/core';
import {DefaultStorageNodeEditorComponent} from 'projects/storage/src/lib/storage-editor/storage-node-editors/default-storage-node-editor/default-storage-node-editor.component';
import {StorageNodeEditorContentService} from 'projects/storage/src/lib/storage-editor/storage-node-editors/storage-node-editor-content.service';
import {STORAGE_NODE} from 'projects/storage/src/lib/storage-editor/storage-node-editors/storage-node-editor';
import {StorageNode} from 'projects/storage/src/lib/entities/storage-node';

@Component({
  selector: 'lib-markdown-storage-node-editor',
  templateUrl: './markdown-storage-node-editor.component.html',
  styleUrls: ['./markdown-storage-node-editor.component.scss'],
  providers: [StorageNodeEditorContentService],
})
export class MarkdownStorageNodeEditorComponent extends DefaultStorageNodeEditorComponent {

  constructor(@Inject(STORAGE_NODE) node: StorageNode,
              contentService: StorageNodeEditorContentService) {
    super(node, contentService);
  }

}
