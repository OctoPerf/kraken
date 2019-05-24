import {Component} from '@angular/core';
import {library} from '@fortawesome/fontawesome-svg-core';
import {faFileImport} from '@fortawesome/free-solid-svg-icons';
import {IconFa} from 'projects/icon/src/lib/icon-fa';
import {StorageTreeControlService} from 'projects/storage/src/lib/storage-tree/storage-tree-control.service';
import {SimulationService} from 'projects/gatling/src/app/simulations/simulation.service';

library.add(faFileImport);

@Component({
  selector: 'app-import-har-menu-item',
  templateUrl: './import-har-menu-item.component.html',
})
export class ImportHarMenuItemComponent {

  readonly importFileIcon = new IconFa(faFileImport);

  constructor(public treeControl: StorageTreeControlService,
              public simulation: SimulationService) {
  }

}


