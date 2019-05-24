import {Injectable, OnDestroy} from '@angular/core';
import {StorageService} from 'projects/storage/src/lib/storage.service';
import {NodeEventToNodePipe} from 'projects/storage/src/lib/storage-pipes/node-event-to-node.pipe';
import {EventBusService} from 'projects/event/src/lib/event-bus.service';
import {DebugChunk} from 'projects/analysis/src/lib/entities/debug-chunk';
import {ResultsListService} from 'projects/analysis/src/lib/results/results-list.service';
import {AnalysisConfigurationService} from 'projects/analysis/src/lib/analysis-configuration.service';
import {Subscription} from 'rxjs';
import {SelectNodeEvent} from 'projects/storage/src/lib/events/select-node-event';
import {map} from 'rxjs/operators';
import {StorageNode} from 'projects/storage/src/lib/entities/storage-node';
import {DebugChunkToPathPipe} from 'projects/analysis/src/lib/results/debug/debug-pipes/debug-chunk-to-path.pipe';
import {IsDebugChunkStorageNodePipe} from 'projects/analysis/src/lib/results/is-debug-chunk-storage-node.pipe';
import {CompareDialogComponent} from 'projects/analysis/src/lib/results/debug/compare/compare-dialog/compare-dialog.component';
import {DialogService} from 'projects/dialog/src/lib/dialog.service';
import {DialogSize} from 'projects/dialog/src/lib/dialog-size';
import {StorageJsonService} from 'projects/storage/src/lib/storage-json.service';
import {StorageListService} from 'projects/storage/src/lib/storage-list.service';
import {Result} from 'projects/analysis/src/lib/entities/result';

@Injectable()
export class DebugChunksListService extends StorageJsonService<DebugChunk> implements OnDestroy {

  public static readonly CHUNK_EXT = '.debug';

  private _selection: DebugChunk | null;
  private _resultSelectionSubscription: Subscription;
  private _nodeSelectionSubscription: Subscription;
  private _result: Result;
  private _lastSelectedNode: StorageNode;

  constructor(
    storage: StorageService,
    storageList: StorageListService,
    private toNode: NodeEventToNodePipe,
    private eventBus: EventBusService,
    private analysisConfiguration: AnalysisConfigurationService,
    private resultsList: ResultsListService,
    private toPath: DebugChunkToPathPipe,
    private dialogs: DialogService,
  ) {
    super(storage, storageList, node => {
      const result = node.path.match(IsDebugChunkStorageNodePipe.PATH_REGEXP);
      return result ? `${result[2]}_${result[1]}` : '';
    }, value => `${value.id}_${value.resultId}`);

    this._resultSelectionSubscription = this.resultsList.selectionChanged.subscribe(this.init.bind(this));
    this._nodeSelectionSubscription = this.eventBus.of<SelectNodeEvent>(SelectNodeEvent.CHANNEL)
      .pipe(map(this.toNode.transform))
      .subscribe(node => this._lastSelectedNode = node);
  }

  ngOnDestroy() {
    super.clearSubscriptions();
    this._resultSelectionSubscription.unsubscribe();
  }

  public init() {
    this.selection = null;

    const result = this.resultsList.selection;
    if (!result || result === this._result) {
      return;
    }

    this.values = [];
    this._result = result;

    super.init(`${this.analysisConfiguration.analysisRootNode.path}/${this._result.id}`, '.*\\.debug', 2);

    this._subscriptions.push(this.eventBus.of<SelectNodeEvent>(SelectNodeEvent.CHANNEL)
      .pipe(map(this.toNode.transform))
      .subscribe(this._selectNode.bind(this)));
  }

  public open(chunk: DebugChunk) {
    const path = `${this.toPath.transform(chunk)}/${chunk.id}${DebugChunksListService.CHUNK_EXT}`;
    this.storage.get(path).subscribe(node => this.storage.edit(node));
    this._selection = chunk;
  }

  public compare() {
    this.dialogs.open(CompareDialogComponent, DialogSize.SIZE_FULL, {
      left: this._selection,
      right: this._selection,
      results: this.resultsList.values,
    }).subscribe();
  }

  public set selection(chunk: DebugChunk) {
    this._selection = chunk;
  }

  public get selection(): DebugChunk {
    return this._selection;
  }

  public isSelected(chunk: DebugChunk): boolean {
    return !!this._selection && this._selection === chunk;
  }

  protected _nodesListed(nodes: StorageNode[]) {
    this.storage.listJSON<DebugChunk>(nodes).subscribe((values: DebugChunk[]) => {
      this.values = values;
      this._selectNode(this._lastSelectedNode);
    });
  }

  private _selectNode(node: StorageNode) {
    if (!node) {
      this._selection = null;
      return;
    }
    const chunk = this.find(node);
    if (chunk) {
      this._selection = chunk;
    }
  }

}
