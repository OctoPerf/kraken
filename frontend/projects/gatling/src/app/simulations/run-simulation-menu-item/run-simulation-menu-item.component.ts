import {Component, Inject} from '@angular/core';
import {StorageTreeControlService} from 'projects/storage/src/lib/storage-tree/storage-tree-control.service';
import {SimulationService} from 'projects/gatling/src/app/simulations/simulation.service';
import {PLAY_ICON} from 'projects/icon/src/lib/icons';
import {KeyBoundMenuItemDirective} from 'projects/storage/src/lib/storage-menu/menu-items/key-bound-menu-item.directive';
import {STORAGE_ID} from 'projects/storage/src/lib/storage-id';
import {KeyBinding, KeyBindingsService} from 'projects/tools/src/lib/key-bindings.service';

@Component({
  selector: 'app-run-simulation-menu-item',
  templateUrl: './run-simulation-menu-item.component.html',
})
export class RunSimulationMenuItemComponent extends KeyBoundMenuItemDirective {

  readonly runIcon = PLAY_ICON;

  constructor(@Inject(STORAGE_ID) private id: string,
              public treeControl: StorageTreeControlService,
              private simulation: SimulationService,
              keys: KeyBindingsService) {
    super(treeControl, keys, new KeyBinding(['shift + ctrl + X'], () => {
      return this.run();
    }, id));
  }

  public isRunnable(): boolean {
    return this.treeControl.hasSingleSelection && this.simulation.isSimulationNode(this.treeControl.first);
  }

  public run(): boolean {
    if (this.isRunnable()) {
      this.simulation.run(this.treeControl.first);
      return true;
    }
    return false;
  }
}
