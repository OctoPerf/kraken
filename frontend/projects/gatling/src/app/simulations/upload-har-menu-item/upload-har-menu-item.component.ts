import {Component} from '@angular/core';
import {StorageTreeControlService} from 'projects/storage/src/lib/storage-tree/storage-tree-control.service';
import {SimulationService} from 'projects/gatling/src/app/simulations/simulation.service';
import {IconFa} from 'projects/icon/src/lib/icon-fa';
import {faFileUpload} from '@fortawesome/free-solid-svg-icons';
import {library} from '@fortawesome/fontawesome-svg-core';

library.add(faFileUpload);

@Component({
  selector: 'app-upload-har-menu-item',
  templateUrl: './upload-har-menu-item.component.html',
})
export class UploadHarMenuItemComponent {

  readonly uploadFileIcon = new IconFa(faFileUpload);

  constructor(public treeControl: StorageTreeControlService,
              public simulation: SimulationService) {
  }

}
