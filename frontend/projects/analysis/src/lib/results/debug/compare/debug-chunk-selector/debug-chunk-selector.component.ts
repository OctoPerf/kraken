import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Result} from 'projects/analysis/src/lib/entities/result';
import {DebugChunk} from 'projects/analysis/src/lib/entities/debug-chunk';
import * as _ from 'lodash';

@Component({
  selector: 'lib-debug-chunk-selector',
  templateUrl: './debug-chunk-selector.component.html',
  styleUrls: ['./debug-chunk-selector.component.scss']
})
export class DebugChunkSelectorComponent implements OnInit {

  @Input() results: Result[];
  @Input() debugChunks: DebugChunk[];
  @Input() debugChunkId: string;
  @Input() resultId: string;
  @Output() debugSelected: EventEmitter<DebugChunk> = new EventEmitter();

  private _sortedChunks: DebugChunk[];

  public chunk: DebugChunk;
  public result: Result;

  ngOnInit() {
    this._sortedChunks = _.sortBy(this.debugChunks, 'date');
    this.debugChunkSelected(this.resultId + ':' + this.debugChunkId);
  }

  filter(resultId: string): DebugChunk[] {
    return _.filter(this._sortedChunks, {resultId});
  }

  debugChunkSelected(compositeId: string) {
    const ids = compositeId.split(':', 2);
    const resultId = ids[0];
    const chunkId = ids[1];
    const chunk: DebugChunk = _.find(this._sortedChunks, {resultId: resultId, id: chunkId}) as DebugChunk;
    this.result = _.find(this.results, {id: resultId});
    this.debugSelected.emit(chunk);
  }
}
