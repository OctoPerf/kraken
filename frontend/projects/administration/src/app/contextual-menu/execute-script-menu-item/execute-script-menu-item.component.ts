import {Component} from '@angular/core';
import {PLAY_ICON} from 'projects/icon/src/lib/icons';
import {CommandService} from 'projects/command/src/lib/command.service';
import {StorageTreeControlService} from 'projects/storage/src/lib/storage-tree/storage-tree-control.service';
import {StorageNodeToNamePipe} from 'projects/storage/src/lib/storage-pipes/storage-node-to-name.pipe';

@Component({
  selector: 'app-execute-script-menu-item',
  templateUrl: './execute-script-menu-item.component.html',
})
export class ExecuteScriptMenuItemComponent {

  readonly executeCommandIcon = PLAY_ICON;

  constructor(public commands: CommandService,
              public treeControl: StorageTreeControlService,
              private toName: StorageNodeToNamePipe) {
  }

  execute() {
    const path = this.treeControl.selectedDirectory.path;
    const name = this.toName.transform(this.treeControl.first);
    this.commands.executeScript(path, name).subscribe();
  }
}
