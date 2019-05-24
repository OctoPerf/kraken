import {Component} from '@angular/core';
import {StorageService} from 'projects/storage/src/lib/storage.service';
import {RENAME_ICON} from 'projects/icon/src/lib/icons';
import {StorageTreeDataSourceService} from 'projects/storage/src/lib/storage-tree/storage-tree-data-source.service';
import {StorageTreeControlService} from 'projects/storage/src/lib/storage-tree/storage-tree-control.service';

@Component({
  selector: 'lib-rename-menu-item',
  templateUrl: './rename-menu-item.component.html',
})
export class RenameMenuItemComponent {

  readonly renameIcon = RENAME_ICON;

  constructor(public dataSource: StorageTreeDataSourceService,
              public storage: StorageService,
              public treeControl: StorageTreeControlService) {
  }

}
