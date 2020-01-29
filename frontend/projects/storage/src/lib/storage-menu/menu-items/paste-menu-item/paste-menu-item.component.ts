import {Component, Inject} from '@angular/core';
import {IconFa} from 'projects/icon/src/lib/icon-fa';
import {StorageTreeControlService} from 'projects/storage/src/lib/storage-tree/storage-tree-control.service';
import {CopyPasteService} from 'projects/storage/src/lib/storage-tree/copy-paste.service';
import {KeyBinding, KeyBindingsService} from 'projects/tools/src/lib/key-bindings.service';
import {KeyBoundMenuItem} from 'projects/storage/src/lib/storage-menu/menu-items/key-bound-menu-item';
import {faPaste} from '@fortawesome/free-solid-svg-icons';
import {library} from '@fortawesome/fontawesome-svg-core';
import {STORAGE_ID} from 'projects/storage/src/lib/storage-id';

library.add(faPaste);

@Component({
  selector: 'lib-paste-menu-item',
  templateUrl: './paste-menu-item.component.html',
  styles: []
})
export class PasteMenuItemComponent extends KeyBoundMenuItem {

  readonly pasteIcon = new IconFa(faPaste);

  constructor(@Inject(STORAGE_ID) private id: string,
              public treeControl: StorageTreeControlService,
              public copyPaste: CopyPasteService,
              keys: KeyBindingsService) {
    super(treeControl, keys, new KeyBinding(['ctrl + v'], () => {
      if (this.treeControl.hasSingleOrNoSelection) {
        this.copyPaste.paste(this.treeControl.selectedDirectory);
        return true;
      }
      return false;
    }, id));
  }

}
