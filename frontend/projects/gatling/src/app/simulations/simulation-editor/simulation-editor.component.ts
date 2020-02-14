import {Component, Inject, OnDestroy, OnInit} from '@angular/core';
import {StorageNodeEditorContentService} from 'projects/storage/src/lib/storage-editor/storage-node-editors/storage-node-editor-content.service';
import {DefaultStorageNodeEditorComponent} from 'projects/storage/src/lib/storage-editor/storage-node-editors/default-storage-node-editor/default-storage-node-editor.component';
import {DEBUG_ICON, PLAY_ICON} from 'projects/icon/src/lib/icons';
import {STORAGE_NODE} from 'projects/storage/src/lib/storage-editor/storage-node-editors/storage-node-editor';
import {StorageNode} from 'projects/storage/src/lib/entities/storage-node';
import {SimulationService} from 'projects/gatling/src/app/simulations/simulation.service';
import {GatlingVariablesAutoCompleter} from 'projects/gatling/src/app/simulations/simulation-editor/completion/gatling-variables-auto-completer';
import {KeyBinding, KeyBindingsService} from 'projects/tools/src/lib/key-bindings.service';

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

  private keyBindings: KeyBinding[] = [];

  constructor(@Inject(STORAGE_NODE) node: StorageNode,
              contentService: StorageNodeEditorContentService,
              private simulation: SimulationService,
              private autoCompleter: GatlingVariablesAutoCompleter) {
    super(node, contentService);
    // key binding use for api Ace
    this.keyBindings.push(new KeyBinding(['Ctrl-Shift-X'], this.runSelection.bind(this)));
    this.keyBindings.push(new KeyBinding(['Ctrl-Shift-D'], this.debugSelection.bind(this)));
  }

  public runSelection(): boolean {
    this.simulation.run(this.node);
    return true;
  }

  public debugSelection(): boolean {
    this.simulation.debug(this.node);
    return true;
  }
}
