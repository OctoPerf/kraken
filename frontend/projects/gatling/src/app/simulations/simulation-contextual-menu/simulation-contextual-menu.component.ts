import {Component} from '@angular/core';
import {StorageTreeControlService} from 'projects/storage/src/lib/storage-tree/storage-tree-control.service';
import {SimulationService} from 'projects/gatling/src/app/simulations/simulation.service';

@Component({
  selector: 'app-simulation-contextual-menu',
  templateUrl: './simulation-contextual-menu.component.html',
})
export class SimulationContextualMenuComponent {

  constructor(public treeControl: StorageTreeControlService,
              public simulation: SimulationService) {
  }

  get showDivider(): boolean {
    return (this.treeControl.hasSingleSelection && this.simulation.isSimulationNode(this.treeControl.first)) // Run simulation
      || (this.treeControl.hasSingleSelection && this.simulation.isHarNode(this.treeControl.first)) // Import Har
      || !this.treeControl.hasSelection; // Upload Har
  }

}
