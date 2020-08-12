import {Component, Inject, OnInit} from '@angular/core';
import {StorageTreeControlService} from 'projects/storage/src/lib/storage-tree/storage-tree-control.service';
import {SimulationService} from 'projects/gatling/src/app/simulations/simulation.service';
import {DEBUG_ICON, PLAY_ICON} from 'projects/icon/src/lib/icons';
import {KeyBoundMenuItemDirective} from 'projects/storage/src/lib/storage-menu/menu-items/key-bound-menu-item.directive';
import {STORAGE_ID} from 'projects/storage/src/lib/storage-id';
import {KeyBinding, KeyBindingsService} from 'projects/tools/src/lib/key-bindings.service';

@Component({
  selector: 'app-debug-simulation-menu-item',
  templateUrl: './debug-simulation-menu-item.component.html',
})
export class DebugSimulationMenuItemComponent  extends KeyBoundMenuItemDirective {

  readonly debugIcon = DEBUG_ICON;

  constructor(@Inject(STORAGE_ID) private id: string,
              public treeControl: StorageTreeControlService,
              private simulation: SimulationService,
              keys: KeyBindingsService) {
    super(treeControl, keys, new KeyBinding(['shift + ctrl + D'], () => {
      return this.debug();
    }, id));
  }

  public canDebug(): boolean {
    return this.treeControl.hasSingleSelection && this.simulation.isSimulationNode(this.treeControl.first);
  }

  public debug(): boolean {
    if (this.canDebug()) {
      this.simulation.debug(this.treeControl.first);
      return true;
    }
    return false;
  }
}
