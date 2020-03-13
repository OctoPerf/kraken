import {StorageService} from 'projects/storage/src/lib/storage.service';
import {StorageListService} from 'projects/storage/src/lib/storage-list.service';
import {StorageNode} from 'projects/storage/src/lib/entities/storage-node';
import {BehaviorSubject, Subscription} from 'rxjs';
import * as _ from 'lodash';

export abstract class StorageJsonService<T> {

  public readonly valuesSubject: BehaviorSubject<T[]> = new BehaviorSubject([]);
  protected readonly _subscriptions: Subscription[] = [];
  protected _loading = false;

  protected constructor(protected storage: StorageService,
                        protected storageList: StorageListService,
                        protected readonly nodeToId: (node: StorageNode) => string,
                        protected readonly valueToId: (value: T) => string) {
  }

  protected init(rootPath: string,
                 matcher: string,
                 maxDepth: number) {
    // Clear subscriptions
    this.clearSubscriptions();

    // Convert nodes into T
    this._subscriptions.push(this.storageList.nodesListed.subscribe(this._nodesListed.bind(this)));
    this._subscriptions.push(this.storageList.nodeCreated.subscribe(this._nodeCreated.bind(this)));
    this._subscriptions.push(this.storageList.nodeModified.subscribe(this._nodeModified.bind(this)));
    this._subscriptions.push(this.storageList.nodesDeleted.subscribe(this._nodesDeleted.bind(this)));

    // Initialise list service
    this.storageList.init(rootPath, matcher, maxDepth);
  }

  find(node: StorageNode): T {
    const id = this.nodeToId(node);
    return this.get(id);
  }

  get(id: string): T {
    return _.find(this.values, value => this.valueToId(value) === id);
  }

  get loading(): boolean {
    return this._loading;
  }

  protected clearSubscriptions() {
    _.invokeMap(this._subscriptions, 'unsubscribe');
  }

  protected _nodesListed(nodes: StorageNode[]) {
    this._loading = true;
    console.log("listed");
    this.storage.listJSON<T>(nodes).subscribe((values: T[]) => {
      this.values = values;
      this._loading = false;
    });
  }

  protected _nodeCreated(node: StorageNode) {
    this.storage.getJSON<T>(node).subscribe((value: T) => {
      const id = this.valueToId(value);
      const values = this.values;
      // Prevent duplicate nodes
      _.remove(values, current => this.valueToId(current) === id);
      values.push(value);
      this.values = values;
    });
  }

  protected _nodesDeleted(nodes: StorageNode[]) {
    const ids = _.map(nodes, (node) => this.nodeToId(node));
    const values = this.values;
    const removed = _.remove(values, value => ids.indexOf(this.valueToId(value)) !== -1);
    if (removed.length) {
      this.values = values;
    }
  }

  protected _nodeModified(node: StorageNode) {
    const id = this.nodeToId(node);
    const values = this.values;
    const index = _.findIndex(values, value => this.valueToId(value) === id);
    if (index !== -1) {
      this.storage.getJSON<T>(node).subscribe((value: T) => {
        values[index] = value;
        this.values = values;
      });
    }
  }

  public set values(values: T[]) {
    this.valuesSubject.next(values);
  }

  public get values(): T[] {
    return this.valuesSubject.value;
  }

}
