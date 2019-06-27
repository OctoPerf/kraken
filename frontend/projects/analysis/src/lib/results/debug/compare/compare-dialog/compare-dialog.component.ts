import {Component, EventEmitter, Inject, OnDestroy} from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import {DebugChunk} from 'projects/analysis/src/lib/entities/debug-chunk';
import {Result} from 'projects/analysis/src/lib/entities/result';
import {DebugChunkToStringPipe} from 'projects/analysis/src/lib/results/debug/debug-pipes/debug-chunk-to-string.pipe';
import {Observable, Subscription, zip} from 'rxjs';
import {debounceTime, flatMap} from 'rxjs/operators';
import * as _ from 'lodash';
import {ResultsListService} from 'projects/analysis/src/lib/results/results-list.service';
import {StorageService} from 'projects/storage/src/lib/storage.service';
import {AnalysisConfigurationService} from 'projects/analysis/src/lib/analysis-configuration.service';
import {StorageNode} from 'projects/storage/src/lib/entities/storage-node';

export interface CompareDialogData {
  left: DebugChunk;
  right: DebugChunk;
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
  public debugChunks: DebugChunk[];

  private _subscriptions: Subscription[] = [];
  _leftDebugChunk: DebugChunk;
  _leftDebugChunkEmitter: EventEmitter<DebugChunk> = new EventEmitter<DebugChunk>();
  _rightDebugChunk: DebugChunk;
  _rightDebugChunkEmitter: EventEmitter<DebugChunk> = new EventEmitter<DebugChunk>();

  constructor(public dialogRef: MatDialogRef<CompareDialogComponent>,
              @Inject(MAT_DIALOG_DATA) public data: CompareDialogData,
              private toString: DebugChunkToStringPipe,
              storage: StorageService,
              analysisConfiguration: AnalysisConfigurationService) {
    storage.find(analysisConfiguration.analysisRootNode.path, `.*\.debug`, 3)
      .pipe(flatMap((nodes: StorageNode[]) => storage.listJSON(nodes)))
      .subscribe((chunks: DebugChunk[]) => {
        this.debugChunks = chunks;
        this.loading = false;
      });

    this._subscriptions.push(zip(this._leftDebugChunkEmitter.pipe(flatMap(this.toString.transform.bind(this.toString))) as Observable<string>,
      this._rightDebugChunkEmitter.pipe(flatMap(this.toString.transform.bind(this.toString)))).pipe(debounceTime(500))
      .subscribe((values: [string, string]) => {
        this.left = values[0];
        this.right = values[1];
        this.loadingDiff = false;
      }));
  }

  ngOnDestroy() {
    _.invokeMap(this._subscriptions, 'unsubscribe');
  }

  selectLeft(chunk: DebugChunk) {
    this._leftDebugChunk = chunk;
    this._refresh();
  }

  selectRight(chunk: DebugChunk) {
    this._rightDebugChunk = chunk;
    this._refresh();
  }

  _refresh() {
    this.loadingDiff = true;
    if (this._leftDebugChunk && this._rightDebugChunk) {
      this._leftDebugChunkEmitter.emit(this._leftDebugChunk);
      this._rightDebugChunkEmitter.emit(this._rightDebugChunk);
    }
  }
}


