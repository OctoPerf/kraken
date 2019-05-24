import {Component, Input} from '@angular/core';
import {StorageNode} from 'projects/storage/src/lib/entities/storage-node';
import {PLAY_ICON} from 'projects/icon/src/lib/icons';
import {SimulationService} from 'projects/gatling/src/app/simulations/simulation.service';

@Component({
  selector: 'app-run-simulation-node-button',
  templateUrl: './run-simulation-node-button.component.html',
})
export class RunSimulationNodeButtonComponent {

  readonly runIcon = PLAY_ICON;

  @Input() node: StorageNode;

  constructor(public simulation: SimulationService) {
  }

}
