import {Component} from '@angular/core';
import {CommandService} from 'projects/command/src/lib/command.service';
import {PLAY_ICON} from 'projects/icon/src/lib/icons';
import {StorageTreeControlService} from 'projects/storage/src/lib/storage-tree/storage-tree-control.service';

@Component({
  selector: 'app-execute-shell-menu-item',
  templateUrl: './execute-shell-menu-item.component.html',
})
export class ExecuteShellMenuItemComponent {

  readonly executeShellIcon = PLAY_ICON;

  constructor(public commands: CommandService,
              public treeControl: StorageTreeControlService) { }

}
