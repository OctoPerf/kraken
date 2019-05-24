import {Component, OnInit} from '@angular/core';
import {StorageTreeControlService} from 'projects/storage/src/lib/storage-tree/storage-tree-control.service';
import {SimulationService} from 'projects/gatling/src/app/simulations/simulation.service';
import {DEBUG_ICON, PLAY_ICON} from 'projects/icon/src/lib/icons';

@Component({
  selector: 'app-debug-simulation-menu-item',
  templateUrl: './debug-simulation-menu-item.component.html',
})
export class DebugSimulationMenuItemComponent {

  readonly debugIcon = DEBUG_ICON;

  constructor(public treeControl: StorageTreeControlService,
              public simulation: SimulationService) {
  }

}
