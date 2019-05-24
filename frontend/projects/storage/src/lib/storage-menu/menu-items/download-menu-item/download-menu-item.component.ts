import {Component} from '@angular/core';
import {StorageService} from 'projects/storage/src/lib/storage.service';
import {StorageTreeControlService} from 'projects/storage/src/lib/storage-tree/storage-tree-control.service';
import {IconFa} from 'projects/icon/src/lib/icon-fa';
import {faDownload} from '@fortawesome/free-solid-svg-icons/faDownload';
import {library} from '@fortawesome/fontawesome-svg-core';

library.add(faDownload);

@Component({
  selector: 'lib-download-menu-item',
  templateUrl: './download-menu-item.component.html',
  styles: []
})
export class DownloadMenuItemComponent {

  readonly downloadIcon = new IconFa(faDownload);

  constructor(public storage: StorageService,
              public treeControl: StorageTreeControlService) {
  }

}
