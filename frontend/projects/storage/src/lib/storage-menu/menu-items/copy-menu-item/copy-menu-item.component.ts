import {Component, Inject} from '@angular/core';
import {StorageTreeControlService} from 'projects/storage/src/lib/storage-tree/storage-tree-control.service';
import {CopyPasteService} from 'projects/storage/src/lib/storage-tree/copy-paste.service';
import {IconFa} from 'projects/icon/src/lib/icon-fa';
import {faCopy} from '@fortawesome/free-solid-svg-icons/faCopy';
import {library} from '@fortawesome/fontawesome-svg-core';
import {KeyBoundMenuItem} from 'projects/storage/src/lib/storage-menu/menu-items/key-bound-menu-item';
import {KeyBinding, KeyBindingsService} from 'projects/tools/src/lib/key-bindings.service';
import {STORAGE_ID} from 'projects/storage/src/lib/storage-id';

library.add(faCopy);

@Component({
  selector: 'lib-copy-menu-item',
  templateUrl: './copy-menu-item.component.html',
  styles: []
})
export class CopyMenuItemComponent extends KeyBoundMenuItem {

  readonly copyIcon = new IconFa(faCopy);

  constructor(@Inject(STORAGE_ID) private id: string,
              public treeControl: StorageTreeControlService,
              public copyPaste: CopyPasteService,
              keys: KeyBindingsService) {
    super(treeControl, keys, new KeyBinding(['ctrl + c'], () => this._handleKey(this.copyPaste.copy.bind(this.copyPaste)), id));
  }

}
