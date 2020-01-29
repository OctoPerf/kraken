import {Component, Inject} from '@angular/core';
import {StorageService} from 'projects/storage/src/lib/storage.service';
import {StorageTreeControlService} from 'projects/storage/src/lib/storage-tree/storage-tree-control.service';
import {DELETE_ICON} from 'projects/icon/src/lib/icons';
import {KeyBoundMenuItem} from 'projects/storage/src/lib/storage-menu/menu-items/key-bound-menu-item';
import {KeyBinding, KeyBindingsService} from 'projects/tools/src/lib/key-bindings.service';
import {STORAGE_ID} from 'projects/storage/src/lib/storage-id';

@Component({
  selector: 'lib-delete-menu-item',
  templateUrl: './delete-menu-item.component.html',
  styles: []
})
export class DeleteMenuItemComponent extends KeyBoundMenuItem {

  readonly deleteIcon = DELETE_ICON;

  constructor(@Inject(STORAGE_ID) private id: string,
              public storage: StorageService,
              public treeControl: StorageTreeControlService,
              keys: KeyBindingsService) {
    super(treeControl, keys, new KeyBinding(['Delete'], () => this._handleKey(this.storage.deleteFilesWithConfirm.bind(this.storage)), id));
  }

}
