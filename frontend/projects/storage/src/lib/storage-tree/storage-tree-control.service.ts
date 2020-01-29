import {Inject, Injectable, OnDestroy} from '@angular/core';
import {FlatTreeControl} from '@angular/cdk/tree';
import {StorageNode} from 'projects/storage/src/lib/entities/storage-node';
import {SelectionModel} from '@angular/cdk/collections';
import {EventBusService} from 'projects/event/src/lib/event-bus.service';
import {
  STORAGE_ROOT_NODE,
  StorageTreeDataSourceService
} from 'projects/storage/src/lib/storage-tree/storage-tree-data-source.service';
import * as _ from 'lodash';
import {OpenNodeEvent} from 'projects/storage/src/lib/events/open-node-event';
import {NodeDeletedEvent} from 'projects/storage/src/lib/events/node-deleted-event';
import {LocalStorageService} from 'projects/tools/src/lib/local-storage.service';
import {STORAGE_ID} from 'projects/storage/src/lib/storage-id';
import {Subscription} from 'rxjs';
import {ContextualMenuEvent} from 'projects/storage/src/lib/events/contextual-menu-event';
import {StorageNodeToPredicatePipe} from 'projects/storage/src/lib/storage-pipes/storage-node-to-predicate.pipe';

@Injectable()
export class StorageTreeControlService extends FlatTreeControl<StorageNode> implements OnDestroy {

  _selection: SelectionModel<StorageNode>;
  _lastSelection: StorageNode;

  private readonly expansionId: string;

  private subscriptions: Subscription[] = [];

  constructor(@Inject(STORAGE_ID) private id: string,
              private dataSource: StorageTreeDataSourceService,
              private eventBus: EventBusService,
              private localStorage: LocalStorageService,
              @Inject(STORAGE_ROOT_NODE) private readonly rootNode: StorageNode,
              private toPredicate: StorageNodeToPredicatePipe) {
    super((node: StorageNode) => node.depth - (rootNode.depth + 1), (node: StorageNode) => node.type === 'DIRECTORY');
    this.expansionId = id + '-expansion';
    this._selection = new SelectionModel<StorageNode>(true /* multiple */);
    this.subscriptions.push(this.eventBus.of(NodeDeletedEvent.CHANNEL).subscribe(this._nodeDeleted.bind(this)));
    this.subscriptions.push(this.expansionModel.changed.subscribe(
      () => this.localStorage.setItem(this.expansionId, this.expansionModel.selected)
    ));
  }

  ngOnDestroy() {
    _.invokeMap(this.subscriptions, 'unsubscribe');
  }

  public loadExpansion(data: StorageNode[]) {
    const expanded: StorageNode[] = _.intersectionBy(data, this.localStorage.getItem(this.expansionId, []), 'path');
    _.forEach(expanded, node => this.expansionModel.select(node));
  }

  public nodeClick($event: MouseEvent, node: StorageNode) {
    $event.stopPropagation();
    this._toggleSelection($event, node);
  }

  public mouseNodeDoubleClick($event: MouseEvent, node: StorageNode) {
    $event.stopPropagation();
    this.nodeDoubleClick(node);
  }

  public nodeDoubleClick(node: StorageNode) {
    if (node.type === 'DIRECTORY') {
      this.toggle(node);
    } else {
      this.eventBus.publish(new OpenNodeEvent(node));
    }
  }

  public nodeContextMenu($event: MouseEvent, node?: StorageNode) {
    $event.stopPropagation();
    if (!this.isSelected(node)) {
      this._selection.clear();
      this._lastSelection = node;
    }

    if (node) {
      this._selection.select(node);
    }
    this.eventBus.publish(new ContextualMenuEvent($event, this.id, node));
  }

  public clearSelection() {
    this._selection.clear();
    this._lastSelection = null;
  }

  public clearExpansion() {
    this.expansionModel.clear();
  }

  public isSelected(node: StorageNode) {
    // Do not compare by node reference, but by path
    return !!node && _.findIndex(this._selection.selected, current => current.path === node.path) !== -1;
  }

  public selectNode(node: StorageNode) {
    this._selection.select(node);
    this._lastSelection = node;
  }

  public deselectNode(node: StorageNode, nextNode: StorageNode) {
    this._selection.deselect(node);
    this._lastSelection = nextNode;
  }

  public selectOne(node: StorageNode) {
    this._selection.clear();
    this.selectNode(node);
    let parent = this.dataSource.parentNode(node);
    while (parent.path !== this.rootNode.path) {
      this.expand(parent);
      parent = this.dataSource.parentNode(parent);
    }
  }

  _toggleSelection($event: MouseEvent, node: StorageNode) {
    const mouseEvent = $event as MouseEvent;

    if ((!mouseEvent.ctrlKey && !mouseEvent.shiftKey) || (mouseEvent.shiftKey && !this._lastSelection)) {
      this.selectOne(node);
      return;
    }

    if (mouseEvent.ctrlKey && !mouseEvent.shiftKey) {
      this._selection.toggle(node);
      this._lastSelection = _.last(this._selection.selected);
      return;
    }

    if (mouseEvent.shiftKey) {
      const nodes = this.dataSource.data;
      const lastIndex = _.indexOf(nodes, this._lastSelection);
      const currentIndex = _.indexOf(nodes, node);
      const selection = _.slice(nodes, Math.min(lastIndex, currentIndex), Math.max(lastIndex, currentIndex) + 1);
      this._selection.clear();
      _.forEach(selection, (current) => {
        this._selection.select(current);
      });
      return;
    }
  }

  get selected(): StorageNode[] {
    return this._selection.selected;
  }

  get first(): StorageNode {
    if (!this.hasSelection) {
      // No _selection, return the root directory
      return this.rootNode;
    }
    return this.selected[0];
  }

  get selectedDirectory(): StorageNode {
    const first = this.first;
    if (first.type === 'DIRECTORY') {
      return first;
    }
    // Return the parent of the first node
    const nodes = this.dataSource.data;
    const firstIndex = _.indexOf(nodes, first);
    for (let index = firstIndex; index >= 0; index--) {
      const current = nodes[index];
      if (current.type === 'DIRECTORY' && current.depth === (first.depth - 1)) {
        return current;
      }
    }
    return this.rootNode;
  }

  get hasSelection(): boolean {
    return this._selection.hasValue();
  }

  get hasSingleOrNoSelection(): boolean {
    return this.selected.length <= 1;
  }

  get hasSingleSelection(): boolean {
    return this.selected.length === 1;
  }

  get hasMultiSelection(): boolean {
    return this.selected.length > 1;
  }

  get hasSingleFileSelection(): boolean {
    return this.hasSingleSelection && this.selected[0].type === 'FILE';
  }

  get hasSingleDirectorySelection(): boolean {
    return this.hasSingleSelection && this.selected[0].type === 'DIRECTORY';
  }

  _nodeDeleted(event: NodeDeletedEvent) {
    const predicate = this.toPredicate.transform(event.node);
    if (this._lastSelection && predicate(this._lastSelection)) {
      this._lastSelection = null;
    }
    _.filter(this._selection.selected, predicate).forEach((node: StorageNode) => this._selection.deselect(node));
  }

}
