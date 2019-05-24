import {Component, Inject} from '@angular/core';
import {StorageNodeEditorContentService} from 'projects/storage/src/lib/storage-editor/storage-node-editors/storage-node-editor-content.service';
import {DefaultStorageNodeEditorComponent} from 'projects/storage/src/lib/storage-editor/storage-node-editors/default-storage-node-editor/default-storage-node-editor.component';
import {DEBUG_ICON, PLAY_ICON} from 'projects/icon/src/lib/icons';
import {STORAGE_NODE} from 'projects/storage/src/lib/storage-editor/storage-node-editors/storage-node-editor';
import {StorageNode} from 'projects/storage/src/lib/entities/storage-node';
import {SimulationService} from 'projects/gatling/src/app/simulations/simulation.service';
import {GatlingVariablesAutoCompleter} from 'projects/gatling/src/app/simulations/simulation-editor/completion/gatling-variables-auto-completer';

@Component({
  selector: 'app-simulation-editor',
  templateUrl: './simulation-editor.component.html',
  styleUrls: ['./simulation-editor.component.scss'],
  providers: [
    StorageNodeEditorContentService,
    GatlingVariablesAutoCompleter,
  ]
})
export class SimulationEditorComponent extends DefaultStorageNodeEditorComponent {

  public readonly runIcon = PLAY_ICON;
  public readonly debugIcon = DEBUG_ICON;

  constructor(@Inject(STORAGE_NODE) node: StorageNode,
              contentService: StorageNodeEditorContentService,
              private simulation: SimulationService,
              private autoCompleter: GatlingVariablesAutoCompleter) {
    super(node, contentService);
  }

}
