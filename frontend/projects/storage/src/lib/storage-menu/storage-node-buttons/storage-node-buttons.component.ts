import {Component, Inject} from '@angular/core';
import {StorageNode} from 'projects/storage/src/lib/entities/storage-node';
import {STORAGE_NODE} from 'projects/storage/src/lib/storage-editor/storage-node-editors/storage-node-editor';

@Component({
  selector: 'lib-storage-node-buttons',
  templateUrl: './storage-node-buttons.component.html',
  styleUrls: ['./storage-node-buttons.component.scss']
})
export class StorageNodeButtonsComponent {

  constructor(@Inject(STORAGE_NODE) public node: StorageNode) {
  }
}
