import {Inject, Injectable, InjectionToken, OnDestroy} from '@angular/core';
import {DataSource} from '@angular/cdk/table';
import {StorageNode} from 'projects/storage/src/lib/entities/storage-node';
import {BehaviorSubject, merge, Observable, Subscription} from 'rxjs';
import {StorageTreeControlService} from 'projects/storage/src/lib/storage-tree/storage-tree-control.service';
import {StorageService} from 'projects/storage/src/lib/storage.service';
import {CollectionViewer} from '@angular/cdk/collections';
import * as _ from 'lodash';
import {map} from 'rxjs/operators';
import {StorageNodeToParentPathPipe} from 'projects/storage/src/lib/storage-pipes/storage-node-to-parent-path.pipe';
import {StorageListService} from 'projects/storage/src/lib/storage-list.service';

export const STORAGE_ROOT_NODE = new InjectionToken<StorageNode>('StorageRootNode');

@Injectable()
export class StorageTreeDataSourceService extends DataSource<StorageNode> implements OnDestroy {

  public treeControl: StorageTreeControlService;

  _expandedData = new BehaviorSubject<StorageNode[]>([]);
  _nodesMap: { [key in string]: StorageNode };

  private subscriptions: Subscription[] = [];

  constructor(private storage: StorageService,
              private storageList: StorageListService,
              private toParentPath: StorageNodeToParentPathPipe,
              @Inject(STORAGE_ROOT_NODE) private readonly rootNode: StorageNode) {
    super();
    this._nodesMap = {};
  }

  ngOnDestroy() {
    _.invokeMap(this.subscriptions, 'unsubscribe');
  }

  connect(collectionViewer: CollectionViewer): Observable<StorageNode[]> {
    const changes = [
      collectionViewer.viewChange,
      this.treeControl.expansionModel.changed,
      this.storageList.nodesSubject
    ];
    return merge(...changes).pipe(map(() => {
      this._expandedData.next(this._expandedNodes);
      return this._expandedData.value;
    }));
  }

  disconnect() {
    // no op
  }

  init() {
    const rootPath = this.rootNode.path ? this.rootNode.path + '/' : '';
    this.storageList.init(rootPath);
    this.subscriptions.push(this.storageList.nodesSubject.subscribe(nodes => this.data = nodes));
  }

  parentNode(node: StorageNode): StorageNode {
    const parent = this._nodesMap[this.toParentPath.transform(node)];
    return parent ? parent : this.rootNode;
  }

  get data() {
    return this.storageList.nodes;
  }

  set data(nodes: StorageNode[]) {
    this._nodesMap = _.keyBy(nodes, 'path');
    this.treeControl.dataNodes = nodes;
    this.treeControl.loadExpansion(nodes);
  }

  /**
   * Expand flattened node with current expansion status.
   * The returned list may have different length.
   */
  get _expandedNodes(): StorageNode[] {
    const results: StorageNode[] = [];
    const currentExpand: boolean[] = [];
    currentExpand[0] = true;

    this.data.forEach(node => {
      const depth = node.depth - (this.rootNode.depth + 1);
      let expand = true;
      for (let i = 0; i <= depth; i++) {
        expand = expand && currentExpand[i];
      }
      if (expand) {
        results.push(node);
      }
      if (node.type === 'DIRECTORY') {
        currentExpand[depth + 1] = this.treeControl.isExpanded(node);
      }
    });
    return results;
  }

  indexOf(node: StorageNode): number {
    const nodes = this._expandedData.getValue();
    return _.indexOf(nodes, node);
  }

  findIndex(node: StorageNode): number {
    const nodes = this._expandedData.getValue();
    return _.findIndex(nodes, (current: StorageNode) => current.path === node.path);
  }

}
