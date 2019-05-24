import {Component, Input} from '@angular/core';
import {MENU_ICON} from 'projects/icon/src/lib/icons';
import {StorageNode} from 'projects/storage/src/lib/entities/storage-node';
import {StorageTreeControlService} from 'projects/storage/src/lib/storage-tree/storage-tree-control.service';

@Component({
  selector: 'lib-menu-node-button',
  templateUrl: './menu-node-button.component.html',
})
export class MenuNodeButtonComponent {

  readonly menuIcon = MENU_ICON;

  @Input() node: StorageNode;

  constructor(public treeControl: StorageTreeControlService) {
  }

}
