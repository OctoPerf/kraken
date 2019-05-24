import {Component, Inject, OnInit} from '@angular/core';
import {STORAGE_NODE, StorageNodeEditor} from 'projects/storage/src/lib/storage-editor/storage-node-editors/storage-node-editor';
import {StorageNode} from 'projects/storage/src/lib/entities/storage-node';
import {StorageNodeEditorContentService} from 'projects/storage/src/lib/storage-editor/storage-node-editors/storage-node-editor-content.service';

@Component({
  selector: 'lib-default-storage-node-editor',
  templateUrl: './default-storage-node-editor.component.html',
  styleUrls: ['./default-storage-node-editor.component.scss'],
  providers: [
    StorageNodeEditorContentService,
  ]
})
export class DefaultStorageNodeEditorComponent implements OnInit, StorageNodeEditor {

  constructor(@Inject(STORAGE_NODE) public node: StorageNode,
              public contentService: StorageNodeEditorContentService) {
  }

  ngOnInit() {
    this.contentService.load(this.node);
  }

}
