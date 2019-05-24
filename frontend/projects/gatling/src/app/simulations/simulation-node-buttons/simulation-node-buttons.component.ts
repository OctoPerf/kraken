import {Component, Inject} from '@angular/core';
import {STORAGE_NODE} from 'projects/storage/src/lib/storage-editor/storage-node-editors/storage-node-editor';
import {StorageNode} from 'projects/storage/src/lib/entities/storage-node';

@Component({
  selector: 'app-simulation-node-buttons',
  templateUrl: './simulation-node-buttons.component.html',
})
export class SimulationNodeButtonsComponent {
  constructor(@Inject(STORAGE_NODE) public node: StorageNode) {
  }
}
