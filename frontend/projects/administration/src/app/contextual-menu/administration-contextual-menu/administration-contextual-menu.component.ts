import {Component} from '@angular/core';
import {StorageTreeControlService} from 'projects/storage/src/lib/storage-tree/storage-tree-control.service';

@Component({
  selector: 'app-administration-contextual-menu',
  templateUrl: './administration-contextual-menu.component.html',
  styleUrls: ['./administration-contextual-menu.component.scss']
})
export class AdministrationContextualMenuComponent {

  constructor(public treeControl: StorageTreeControlService) {
  }

}
