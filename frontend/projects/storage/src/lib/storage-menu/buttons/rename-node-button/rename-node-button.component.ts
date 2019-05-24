import {Component, Input} from '@angular/core';
import {StorageNode} from 'projects/storage/src/lib/entities/storage-node';
import {StorageTreeDataSourceService} from 'projects/storage/src/lib/storage-tree/storage-tree-data-source.service';
import {StorageService} from 'projects/storage/src/lib/storage.service';
import {RENAME_ICON} from 'projects/icon/src/lib/icons';

@Component({
  selector: 'lib-rename-node-button',
  templateUrl: './rename-node-button.component.html',
})
export class RenameNodeButtonComponent {

  readonly renameIcon = RENAME_ICON;

  @Input() node: StorageNode;

  constructor(public dataSource: StorageTreeDataSourceService,
              public storage: StorageService) {
  }
}
