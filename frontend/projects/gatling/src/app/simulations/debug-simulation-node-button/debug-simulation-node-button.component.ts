import {Component, Input} from '@angular/core';
import {StorageNode} from 'projects/storage/src/lib/entities/storage-node';
import {DEBUG_ICON, PLAY_ICON} from 'projects/icon/src/lib/icons';
import {SimulationService} from 'projects/gatling/src/app/simulations/simulation.service';

@Component({
  selector: 'app-debug-simulation-node-button',
  templateUrl: './debug-simulation-node-button.component.html',
})
export class DebugSimulationNodeButtonComponent {

  readonly debugIcon = DEBUG_ICON;

  @Input() node: StorageNode;

  constructor(public simulation: SimulationService) {
  }

}
