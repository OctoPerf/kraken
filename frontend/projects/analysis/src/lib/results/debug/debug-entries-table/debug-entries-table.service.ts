import {Injectable, OnDestroy} from '@angular/core';
import {StorageService} from 'projects/storage/src/lib/storage.service';
import {NodeEventToNodePipe} from 'projects/storage/src/lib/storage-pipes/node-event-to-node.pipe';
import {EventBusService} from 'projects/event/src/lib/event-bus.service';
import {DebugEntry} from 'projects/analysis/src/lib/entities/debug-entry';
import {ResultsTableService} from 'projects/analysis/src/lib/results/results-table/results-table.service';
import {AnalysisConfigurationService} from 'projects/analysis/src/lib/analysis-configuration.service';
import {Subscription} from 'rxjs';
import {SelectNodeEvent} from 'projects/storage/src/lib/events/select-node-event';
import {map} from 'rxjs/operators';
import {StorageNode} from 'projects/storage/src/lib/entities/storage-node';
import {PATH_REGEXP} from 'projects/analysis/src/lib/results/is-debug-entry-storage-node.pipe';
import {CompareDialogComponent} from 'projects/analysis/src/lib/results/debug/compare/compare-dialog/compare-dialog.component';
import {DialogSize} from 'projects/dialog/src/lib/dialog-size';
import {StorageJsonService} from 'projects/storage/src/lib/storage-json.service';
import {StorageListService} from 'projects/storage/src/lib/storage-list.service';
import {Result} from 'projects/analysis/src/lib/entities/result';
import {DebugEntryToPathPipe} from 'projects/analysis/src/lib/results/debug/debug-pipes/debug-entry-to-path.pipe';
import {MonoSelectionWrapper} from 'projects/components/src/lib/selection/mono-selection-wrapper';
import {InjectDialogService} from 'projects/dialog/src/lib/inject-dialogs/inject-dialog.service';

@Injectable()
export class DebugEntriesTableService extends StorageJsonService<DebugEntry> implements OnDestroy {

  public static readonly ENTRY_EXT = '.debug';

  public readonly _selection: MonoSelectionWrapper<DebugEntry> = new MonoSelectionWrapper<DebugEntry>((t1, t2) => t1 === t2);
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
    private resultsList: ResultsTableService,
    private toPath: DebugEntryToPathPipe,
    private dialogs: InjectDialogService,
  ) {
    super(storage, storageList, node => {
      const result = node.path.match(PATH_REGEXP);
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
    const result = this.resultsList._selection.selection;
    if (!result || result === this._result) {
      return;
    }
    this._selection.selection = null;
    this.values = [];
    this._result = result;

    super.init(`${this.analysisConfiguration.analysisRootNode.path}/${this._result.id}`, '.*\\.debug', 2, 250);

    this._subscriptions.push(this.eventBus.of<SelectNodeEvent>(SelectNodeEvent.CHANNEL)
      .pipe(map(this.toNode.transform))
      .subscribe(this._selectNode.bind(this)));
  }

  public open(entry: DebugEntry) {
    const path = `${this.toPath.transform(entry)}/${entry.id}${DebugEntriesTableService.ENTRY_EXT}`;
    this.storage.get(path).subscribe(node => this.storage.edit(node));
    this._selection.selection = entry;
  }

  public compare() {
    const selection = this._selection.selection;
    this.dialogs.open(CompareDialogComponent, DialogSize.SIZE_FULL, {
      left: selection,
      right: selection,
      results: this.resultsList.values,
    }).subscribe();
  }

  protected _nodesListed(nodes: StorageNode[]) {
    this.storage.listJSON<DebugEntry>(nodes).subscribe((values: DebugEntry[]) => {
      this.values = values;
      this._selectNode(this._lastSelectedNode);
      this._loading = false;
    });
  }

  private _selectNode(node: StorageNode) {
    if (!node) {
      this._selection.selection = null;
      return;
    }
    const entry = this.find(node);
    if (entry) {
      this._selection.selection = entry;
    }
  }
}
