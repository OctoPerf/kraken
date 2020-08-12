import {Component, Inject} from '@angular/core';
import {StorageNodeEditorContentService} from 'projects/storage/src/lib/storage-editor/storage-node-editors/storage-node-editor-content.service';
import {DefaultStorageNodeEditorComponent} from 'projects/storage/src/lib/storage-editor/storage-node-editors/default-storage-node-editor/default-storage-node-editor.component';
import {DEBUG_ICON, PLAY_ICON} from 'projects/icon/src/lib/icons';
import {STORAGE_NODE} from 'projects/storage/src/lib/storage-editor/storage-node-editors/storage-node-editor';
import {StorageNode} from 'projects/storage/src/lib/entities/storage-node';
import {SimulationService} from 'projects/gatling/src/app/simulations/simulation.service';
import {GatlingVariablesAutoCompleter} from 'projects/gatling/src/app/simulations/simulation-editor/completion/gatling-variables-auto-completer';
import {KeyBinding} from 'projects/tools/src/lib/key-bindings.service';
import {CodeSnippet} from 'projects/editor/src/lib/code-snippet';

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
  private snippets: CodeSnippet[] = [
    {
      tabTrigger: 'pck',
      name: 'pck',
      content: 'package ${1:package_name}\n'
    },
    {
      tabTrigger: 'impg',
      name: 'impg',
      content: 'import io.gatling.${1:package_name}._\n'
    },
    {
      tabTrigger: 'imps',
      name: 'imps',
      content: 'import io.gatling.core.Predef._\nimport io.gatling.http.Predef._\nimport scala.concurrent.duration._\n'
    },
    {
      tabTrigger: 'class',
      name: 'class',
      content: 'class ${1:class_name} extends Simulation {\n' +
        '\t${4:// body...}\n' +
        '}\n'
    },
    {
      tabTrigger: 'obj',
      name: 'obj',
      content: 'object ${1:object_name} {\n' +
        '\t${4:// body...}\n' +
        '}\n'
    },
    {
      tabTrigger: 'reqg',
      name: 'reqg',
      content: 'http("${1:request_name}")\n' +
        '\t.get("${2:url}")\n' +
        '\t.queryParam("${2:param_key}", "${2:param_value}")\n'
    },
    {
      tabTrigger: 'reqp',
      name: 'reqp',
      content: 'http("${1:request_name}")\n' +
        '\t.post("${2:url}")\n' +
        '\t.body(RawFileBody("${3:file}"))\n'
    },
    {
      tabTrigger: 'http',
      name: 'http',
      content: 'http\n' +
        '\t.baseUrl("${1:url}")\n' +
        '\t.header(HttpHeaderNames.ContentType, HttpHeaderValues.ApplicationJson)\n' +
        '\t.header(HttpHeaderNames.Accept, HttpHeaderValues.ApplicationJson)\n'
    }
  ];

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
