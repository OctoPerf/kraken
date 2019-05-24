import {Component, OnInit} from '@angular/core';
import {StorageTreeControlService} from 'projects/storage/src/lib/storage-tree/storage-tree-control.service';
import {SimulationService} from 'projects/gatling/src/app/simulations/simulation.service';
import {PLAY_ICON} from 'projects/icon/src/lib/icons';

@Component({
  selector: 'app-run-simulation-menu-item',
  templateUrl: './run-simulation-menu-item.component.html',
})
export class RunSimulationMenuItemComponent {

  readonly runIcon = PLAY_ICON;

  constructor(public treeControl: StorageTreeControlService,
              public simulation: SimulationService) {
  }

}
