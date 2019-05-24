import {Component} from '@angular/core';
import {CommandService} from 'projects/command/src/lib/command.service';
import {PLAY_ICON} from 'projects/icon/src/lib/icons';
import {StorageTreeControlService} from 'projects/storage/src/lib/storage-tree/storage-tree-control.service';

@Component({
  selector: 'app-execute-command-menu-item',
  templateUrl: './execute-command-menu-item.component.html',
})
export class ExecuteCommandMenuItemComponent {

  readonly executeCommandIcon = PLAY_ICON;

  constructor(public commands: CommandService,
              public treeControl: StorageTreeControlService) { }

}
