import {Component} from '@angular/core';
import {StorageService} from 'projects/storage/src/lib/storage.service';
import {StorageTreeControlService} from 'projects/storage/src/lib/storage-tree/storage-tree-control.service';
import {IconFaAddon} from 'projects/icon/src/lib/icon-fa-addon';
import {IconFa} from 'projects/icon/src/lib/icon-fa';
import {faFile} from '@fortawesome/free-solid-svg-icons/faFile';
import {faPlus} from '@fortawesome/free-solid-svg-icons/faPlus';
import {library} from '@fortawesome/fontawesome-svg-core';

library.add(faFile, faPlus);

@Component({
  selector: 'lib-new-file-menu-item',
  templateUrl: './new-file-menu-item.component.html',
  styles: []
})
export class NewFileMenuItemComponent {

  readonly addFileIcon = new IconFaAddon(
    new IconFa(faFile),
    new IconFa(faPlus, 'success', 'shrink-4 up-7 left-5'),
  );

  constructor(public storage: StorageService,
              public treeControl: StorageTreeControlService) {
  }

}
