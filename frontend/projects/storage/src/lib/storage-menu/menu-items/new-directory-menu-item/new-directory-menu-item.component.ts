import {Component} from '@angular/core';
import {IconFaAddon} from 'projects/icon/src/lib/icon-fa-addon';
import {IconFa} from 'projects/icon/src/lib/icon-fa';
import {faFolder} from '@fortawesome/free-solid-svg-icons/faFolder';
import {faPlus} from '@fortawesome/free-solid-svg-icons/faPlus';
import {library} from '@fortawesome/fontawesome-svg-core';
import {StorageService} from 'projects/storage/src/lib/storage.service';
import {StorageTreeControlService} from 'projects/storage/src/lib/storage-tree/storage-tree-control.service';

library.add(faFolder, faPlus);

@Component({
  selector: 'lib-new-directory-menu-item',
  templateUrl: './new-directory-menu-item.component.html',
})
export class NewDirectoryMenuItemComponent {

  readonly addDirectoryIcon = new IconFaAddon(
    new IconFa(faFolder),
    new IconFa(faPlus, 'success', 'shrink-4 up-6 left-7'),
  );

  constructor(public storage: StorageService,
              public treeControl: StorageTreeControlService) {
  }

}
