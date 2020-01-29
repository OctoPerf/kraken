import {Component, Inject} from '@angular/core';
import {KeyBoundMenuItem} from 'projects/storage/src/lib/storage-menu/menu-items/key-bound-menu-item';
import {IconFa} from 'projects/icon/src/lib/icon-fa';
import {StorageTreeControlService} from 'projects/storage/src/lib/storage-tree/storage-tree-control.service';
import {CopyPasteService} from 'projects/storage/src/lib/storage-tree/copy-paste.service';
import {KeyBinding, KeyBindingsService} from 'projects/tools/src/lib/key-bindings.service';
import {faCut} from '@fortawesome/free-solid-svg-icons/faCut';
import {library} from '@fortawesome/fontawesome-svg-core';
import {STORAGE_ID} from 'projects/storage/src/lib/storage-id';

library.add(faCut);

@Component({
  selector: 'lib-cut-menu-item',
  templateUrl: './cut-menu-item.component.html',
  styles: []
})
export class CutMenuItemComponent extends KeyBoundMenuItem {

  readonly cutIcon = new IconFa(faCut);

  constructor(@Inject(STORAGE_ID) private id: string,
              public treeControl: StorageTreeControlService,
              public copyPaste: CopyPasteService,
              keys: KeyBindingsService) {
    super(treeControl, keys, new KeyBinding(['ctrl + x'], () => this._handleKey(this.copyPaste.cut.bind(this.copyPaste)), id));
  }

}
