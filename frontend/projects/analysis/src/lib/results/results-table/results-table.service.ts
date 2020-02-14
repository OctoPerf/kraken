import {EventEmitter, Injectable, OnDestroy} from '@angular/core';
import {StorageService} from 'projects/storage/src/lib/storage.service';
import {StorageNode} from 'projects/storage/src/lib/entities/storage-node';
import {Result} from 'projects/analysis/src/lib/entities/result';
import {StorageNodeToNamePipe} from 'projects/storage/src/lib/storage-pipes/storage-node-to-name.pipe';
import {EventBusService} from 'projects/event/src/lib/event-bus.service';
import {StorageNodeToParentPathPipe} from 'projects/storage/src/lib/storage-pipes/storage-node-to-parent-path.pipe';
import {NodeEventToNodePipe} from 'projects/storage/src/lib/storage-pipes/node-event-to-node.pipe';
import {AnalysisConfigurationService} from 'projects/analysis/src/lib/analysis-configuration.service';
import {SelectionModel} from '@angular/cdk/collections';
import {LocalStorageService} from 'projects/tools/src/lib/local-storage.service';
import {OpenDebugEvent} from 'projects/analysis/src/lib/events/open-debug-event';
import {SelectNodeEvent} from 'projects/storage/src/lib/events/select-node-event';
import {filter, map} from 'rxjs/operators';
import {IsDebugEntryStorageNodePipe} from 'projects/analysis/src/lib/results/is-debug-entry-storage-node.pipe';
import {StorageJsonService} from 'projects/storage/src/lib/storage-json.service';
import {StorageListService} from 'projects/storage/src/lib/storage-list.service';
import * as _ from 'lodash';

@Injectable()
export class ResultsTableService extends StorageJsonService<Result> implements OnDestroy {

  public static readonly ID = 'results-table-selection';

  public readonly _selection: SelectionModel<Result> = new SelectionModel(false);
  public readonly selectionChanged: EventEmitter<Result> = new EventEmitter();

  constructor(
    storage: StorageService,
    storageList: StorageListService,
    toParentPath: StorageNodeToParentPathPipe,
    toName: StorageNodeToNamePipe,
    private isDebug: IsDebugEntryStorageNodePipe,
    private toNode: NodeEventToNodePipe,
    private analysisConfiguration: AnalysisConfigurationService,
    private localStorage: LocalStorageService,
    private eventBus: EventBusService,
  ) {
    super(storage, storageList, node => {
        const parentPath = toParentPath.transform(node);
        return toName.transform({path: parentPath} as StorageNode);
      },
      value => value.id);
  }

  ngOnDestroy() {
    super.clearSubscriptions();
  }

  public init() {
    super.init(this.analysisConfiguration.analysisRootNode.path, 'result\\.json', 2);

    this._selection.changed.subscribe(() => this.selectionChanged.emit(this.selection));

    // Init _selection on component load
    const defaultResult = this.localStorage.getItem(ResultsTableService.ID);
    if (defaultResult) {
      this.selection = defaultResult;
    }

    // Update local storage on _selection change
    this._subscriptions.push(this._selection.changed.subscribe(result => {
      if (result.added.length) {
        this.localStorage.setItem(ResultsTableService.ID, result.added[0]);
      } else {
        this.localStorage.remove(ResultsTableService.ID);
      }
      // Open debug if it's a debug result
      if (this._selection.hasValue() && this.selection.type !== 'RUN') {
        this.eventBus.publish(new OpenDebugEvent());
      }
    }));

    // Update _selection on list change
    this._subscriptions.push(this.valuesSubject.subscribe(() => {
      if (this.hasSelection) {
        const result = this.get(this.selection.id);
        if (result) {
          this.selection = result;
        } else {
          this._selection.clear();
        }
      }
    }));

    // Update selection on editor change
    this._subscriptions.push(this.eventBus.of<SelectNodeEvent>(SelectNodeEvent.CHANNEL)
      .pipe(map(this.toNode.transform), filter(this.isDebug.transform.bind(this.isDebug)))
      .subscribe((node: StorageNode) => {
        const result = node.path.match(IsDebugEntryStorageNodePipe.PATH_REGEXP);
        const resultId = result[1];
        this.selection = this.get(resultId);
      }));
  }

  public isSelected(result: Result): boolean {
    return this.hasSelection && this.selection.id === result.id;
  }

  public get hasSelection(): boolean {
    return this._selection.hasValue();
  }

  public set selection(result: Result) {
    if (result) {
      this._selection.select(result);
    } else {
      this._selection.clear();
    }
  }

  public get selection(): Result | null {
    return this.hasSelection ? this._selection.selected[0] : null;
  }

  // Select debug nodes automatically
  protected _nodeCreated(node: StorageNode) {
    this.storage.getJSON<Result>(node).subscribe((value: Result) => {
      const id = this.valueToId(value);
      const values = this.values;
      // Prevent duplicate nodes
      _.remove(values, current => this.valueToId(current) === id);
      values.push(value);
      this.values = values;
      if (value.type !== 'RUN') {
        this._selection.select(value);
      }
    });
  }

}
