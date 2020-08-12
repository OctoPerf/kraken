import {Component, Inject} from '@angular/core';
import {StorageService} from 'projects/storage/src/lib/storage.service';
import {RENAME_ICON} from 'projects/icon/src/lib/icons';
import {StorageTreeDataSourceService} from 'projects/storage/src/lib/storage-tree/storage-tree-data-source.service';
import {StorageTreeControlService} from 'projects/storage/src/lib/storage-tree/storage-tree-control.service';
import {KeyBoundMenuItemDirective} from 'projects/storage/src/lib/storage-menu/menu-items/key-bound-menu-item.directive';
import {STORAGE_ID} from 'projects/storage/src/lib/storage-id';
import {KeyBinding, KeyBindingsService} from 'projects/tools/src/lib/key-bindings.service';

@Component({
  selector: 'lib-rename-menu-item',
  templateUrl: './rename-menu-item.component.html',
})
export class RenameMenuItemComponent extends KeyBoundMenuItemDirective {

  readonly renameIcon = RENAME_ICON;

  constructor(@Inject(STORAGE_ID) private id: string,
              private dataSource: StorageTreeDataSourceService,
              private storage: StorageService,
              public treeControl: StorageTreeControlService,
              keys: KeyBindingsService) {
    super(treeControl, keys, new KeyBinding(['F2'], () => {
      return this.rename();
    }, id));
  }

  public canRename(): boolean {
    return this.treeControl.hasSingleSelection;
  }

  public rename(): boolean {
    if (this.canRename()) {
      this.storage.rename(this.treeControl.first, this.dataSource.parentNode(this.treeControl.first));
      return true;
    }
    return false;
  }
}
