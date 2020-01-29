import {Component, Inject, InjectionToken, OnDestroy, Optional} from '@angular/core';
import {StorageNode} from 'projects/storage/src/lib/entities/storage-node';
import {LocalStorageService} from 'projects/tools/src/lib/local-storage.service';
import {StorageService} from 'projects/storage/src/lib/storage.service';
import {BehaviorSubject, Subscription} from 'rxjs';
import {EventBusService} from 'projects/event/src/lib/event-bus.service';
import {OpenNodeEvent} from 'projects/storage/src/lib/events/open-node-event';
import * as _ from 'lodash';
import {WindowService} from 'projects/tools/src/lib/window.service';
import {STORAGE_ID} from 'projects/storage/src/lib/storage-id';
import {SelectNodeEvent} from 'projects/storage/src/lib/events/select-node-event';
import {StorageEditorService} from 'projects/storage/src/lib/storage-editor/storage-editor.service';
import {ComponentPortal} from '@angular/cdk/portal';
import {StorageNodeEditor} from 'projects/storage/src/lib/storage-editor/storage-node-editors/storage-node-editor';
import {NodeDeletedEvent} from 'projects/storage/src/lib/events/node-deleted-event';
import {NodePendingSaveEvent} from 'projects/storage/src/lib/events/node-pending-save-event';
import {KeyBinding, KeyBindingsService} from 'projects/tools/src/lib/key-bindings.service';
import {SaveNodeEvent} from 'projects/storage/src/lib/events/save-node-event';
import {StorageNodeToPredicatePipe} from 'projects/storage/src/lib/storage-pipes/storage-node-to-predicate.pipe';
import {StorageConfigurationService} from 'projects/storage/src/lib/storage-configuration.service';
import {SelectHelpEvent} from 'projects/help/src/lib/help-panel/select-help-event';

export const STORAGE_EDITOR_README_NODE = new InjectionToken<StorageNode>('StorageEditorReadmeNode');

@Component({
  selector: 'lib-storage-editor',
  templateUrl: './storage-editor.component.html',
  styleUrls: ['./storage-editor.component.scss']
})
export class StorageEditorComponent implements OnDestroy {

  public readonly selectedIndex: BehaviorSubject<number>;
  public readonly nodes: BehaviorSubject<StorageNode[]>;
  public editors: { [key in string]: ComponentPortal<StorageNodeEditor> } = {};

  _saveKeyBinding: KeyBinding;

  private readonly indexId: string;
  private readonly nodesId: string;

  private pendingSaveMap: { [key in string]: boolean } = {}; // node.path => pendingSave state
  private subscriptions: Subscription[] = [];

  constructor(@Inject(STORAGE_ID) private id: string,
              @Inject(STORAGE_EDITOR_README_NODE) @Optional() readmeNode: StorageNode,
              private localStorage: LocalStorageService,
              private storage: StorageService,
              private eventBus: EventBusService,
              public windowService: WindowService,
              private editorService: StorageEditorService,
              private keys: KeyBindingsService,
              private toPredicate: StorageNodeToPredicatePipe,
              storageConfiguration: StorageConfigurationService) {
    // Init ids
    this.indexId = this.id + '-index';
    this.nodesId = this.id + '-nodes';

    // Bind _openNode to bus event
    this.subscriptions.push(this.eventBus.of(OpenNodeEvent.CHANNEL).subscribe(this._openNode.bind(this)));
    this.subscriptions.push(this.eventBus.of(NodeDeletedEvent.CHANNEL).subscribe(this._closeDeletedNode.bind(this)));
    this.subscriptions.push(this.eventBus.of(NodePendingSaveEvent.CHANNEL).subscribe(this._tagPendingSaveNode.bind(this)));

    this._saveKeyBinding = new KeyBinding(['ctrl + s'], () => {
      if (this.nodes.value.length) {
        this.eventBus.publish(new SaveNodeEvent(this.nodes.value[this.selectedIndex.value]));
      }
      return true;
    }, null, true, true, ['UNFOCUSED']);
    this.keys.add([this._saveKeyBinding]);

    const nodes = this.localStorage.getItem(this.nodesId, []);
    const selectedIndex = this.localStorage.getNumber(this.indexId, 0);

    // Init subjects with blank values
    this.selectedIndex = new BehaviorSubject<number>(0);
    this.nodes = new BehaviorSubject<StorageNode[]>([]);

    // Update local storage on subjects change
    this.selectedIndex.subscribe((index) => this.localStorage.set(this.indexId, index));
    this.nodes.subscribe((_nodes) => this.localStorage.setItem(this.nodesId, _nodes));

    // Filter existing nodes
    this.storage.filterExisting(nodes).subscribe((existingNodes: StorageNode[]) => {
      if (existingNodes.length === 0) {
        this.nodes.next([storageConfiguration.readmeNode]);
        this.selectedIndex.next(0);
      } else {
        this.nodes.next(existingNodes);
        this.selectedIndex.next(Math.max(existingNodes.length - 1, selectedIndex));
      }
    });
  }

  ngOnDestroy() {
    _.invokeMap(this.subscriptions, 'unsubscribe');
    this.keys.remove([this._saveKeyBinding]);
  }

  public closeNode(index: number) {
    const nodes = this.nodes.value;
    nodes.splice(index, 1);
    this.nodes.next(nodes);
    if (this.selectedIndex.value === index) {
      this.selectIndex(Math.min(Math.max(index, 0), nodes.length - 1));
    }
  }

  public selectIndex(index: number) {
    this.selectedIndex.next(index);
    const node = this.nodes.value[index];
    if (node) {
      this.eventBus.publish(new SelectNodeEvent(node));
      this.eventBus.publish(new SelectHelpEvent(this.editorService.getHelpPageId(node)));
    }
  }

  public getNodeEditor(node: StorageNode): ComponentPortal<StorageNodeEditor> {
    let editor = this.editors[node.path];
    if (editor) {
      return editor;
    }
    editor = this.editorService.getNodeEditor(node);
    this.editors[node.path] = editor;
    return editor;
  }

  public isPendingSave(node: StorageNode): boolean {
    return this.pendingSaveMap[node.path];
  }

  _tagPendingSaveNode(event: NodePendingSaveEvent) {
    this.pendingSaveMap[event.node.path] = event.pendingSave;
  }

  _openNode(event: OpenNodeEvent) {
    const node = event.node;
    const nodes = this.nodes.value;
    const index = _.findIndex(nodes, {path: node.path});
    if (index !== -1) {
      this.selectedIndex.next(index);
    } else {
      nodes.push(node);
      this.nodes.next(nodes);
      this.selectedIndex.next(nodes.length - 1);
    }
  }

  _closeDeletedNode(event: NodeDeletedEvent) {
    const nodes = this.nodes.value;
    const predicate = this.toPredicate.transform(event.node);
    _.filter(nodes, predicate).forEach((node: StorageNode) => {
      const index = _.indexOf(nodes, node);
      this.closeNode(index);
    });
  }

  closeOthers() {
    const nodes = this.nodes.value;
    this.nodes.next([nodes[this.selectedIndex.value]]);
    this.selectedIndex.next(0);
  }

  closeAll() {
    this.nodes.next([]);
    this.selectedIndex.next(-1);
  }

}
