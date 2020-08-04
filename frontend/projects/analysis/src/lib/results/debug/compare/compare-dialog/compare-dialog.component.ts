import {Component, EventEmitter, Inject, OnDestroy} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {DebugEntry} from 'projects/analysis/src/lib/entities/debug-entry';
import {Result} from 'projects/analysis/src/lib/entities/result';
import {Observable, Subscription, zip} from 'rxjs';
import {debounceTime, mergeMap} from 'rxjs/operators';
import * as _ from 'lodash';
import {StorageService} from 'projects/storage/src/lib/storage.service';
import {AnalysisConfigurationService} from 'projects/analysis/src/lib/analysis-configuration.service';
import {StorageNode} from 'projects/storage/src/lib/entities/storage-node';
import {DebugEntryToStringPipe} from 'projects/analysis/src/lib/results/debug/debug-pipes/debug-entry-to-string.pipe';

export interface CompareDialogData {
  left: DebugEntry;
  right: DebugEntry;
  results: Result[];
}

@Component({
  selector: 'lib-compare-dialog',
  templateUrl: './compare-dialog.component.html',
  styleUrls: ['./compare-dialog.component.scss']
})
export class CompareDialogComponent implements OnDestroy {

  public loading = true;
  public loadingDiff = true;
  public left: string;
  public right: string;
  public debugEntries: DebugEntry[];

  private _subscriptions: Subscription[] = [];
  _leftDebugEntry: DebugEntry;
  _leftDebugEntryEmitter: EventEmitter<DebugEntry> = new EventEmitter<DebugEntry>();
  _rightDebugEntry: DebugEntry;
  _rightDebugEntryEmitter: EventEmitter<DebugEntry> = new EventEmitter<DebugEntry>();

  constructor(public dialogRef: MatDialogRef<CompareDialogComponent>,
              @Inject(MAT_DIALOG_DATA) public data: CompareDialogData,
              private toString: DebugEntryToStringPipe,
              storage: StorageService,
              analysisConfiguration: AnalysisConfigurationService) {
    storage.find(analysisConfiguration.analysisRootNode.path, `.*\.debug`, 3)
      .pipe(mergeMap((nodes: StorageNode[]) => storage.listJSON(nodes)))
      .subscribe((entries: DebugEntry[]) => {
        this.debugEntries = entries;
        this.loading = false;
      });

    this._subscriptions.push(zip(this._leftDebugEntryEmitter.pipe(mergeMap(this.toString.transform.bind(this.toString))) as Observable<string>,
      this._rightDebugEntryEmitter.pipe(mergeMap(this.toString.transform.bind(this.toString)))).pipe(debounceTime(500))
      .subscribe((values: [string, string]) => {
        this.left = values[0];
        this.right = values[1];
        this.loadingDiff = false;
      }));
  }

  ngOnDestroy() {
    _.invokeMap(this._subscriptions, 'unsubscribe');
  }

  selectLeft(entry: DebugEntry) {
    this._leftDebugEntry = entry;
    this._refresh();
  }

  selectRight(entry: DebugEntry) {
    this._rightDebugEntry = entry;
    this._refresh();
  }

  _refresh() {
    this.loadingDiff = true;
    if (this._leftDebugEntry && this._rightDebugEntry) {
      this._leftDebugEntryEmitter.emit(this._leftDebugEntry);
      this._rightDebugEntryEmitter.emit(this._rightDebugEntry);
    }
  }
}


