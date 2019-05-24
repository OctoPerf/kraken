import {Component} from '@angular/core';
import {StorageService} from 'projects/storage/src/lib/storage.service';
import {StorageTreeControlService} from 'projects/storage/src/lib/storage-tree/storage-tree-control.service';
import {IconFa} from 'projects/icon/src/lib/icon-fa';
import {faUpload} from '@fortawesome/free-solid-svg-icons/faUpload';
import {library} from '@fortawesome/fontawesome-svg-core';

library.add(faUpload);

@Component({
  selector: 'lib-upload-menu-item',
  templateUrl: './upload-menu-item.component.html',
  styles: []
})
export class UploadMenuItemComponent {

  readonly uploadFileIcon = new IconFa(faUpload);

  constructor(public storage: StorageService,
              public treeControl: StorageTreeControlService) {
  }
}
