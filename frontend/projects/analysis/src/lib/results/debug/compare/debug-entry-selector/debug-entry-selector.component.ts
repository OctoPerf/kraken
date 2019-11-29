import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Result} from 'projects/analysis/src/lib/entities/result';
import {DebugEntry} from 'projects/analysis/src/lib/entities/debug-entry';
import * as _ from 'lodash';

@Component({
  selector: 'lib-debug-entry-selector',
  templateUrl: './debug-entry-selector.component.html',
  styleUrls: ['./debug-entry-selector.component.scss']
})
export class DebugEntrySelectorComponent implements OnInit {

  @Input() results: Result[];
  @Input() debugEntries: DebugEntry[];
  @Input() debugEntryId: string;
  @Input() resultId: string;
  @Output() debugSelected: EventEmitter<DebugEntry> = new EventEmitter();

  private _sortedEntries: DebugEntry[];

  public entry: DebugEntry;
  public result: Result;

  ngOnInit() {
    this._sortedEntries = _.sortBy(this.debugEntries, 'date');
    this.debugEntrySelected(this.resultId + ':' + this.debugEntryId);
  }

  filter(resultId: string): DebugEntry[] {
    return _.filter(this._sortedEntries, {resultId});
  }

  debugEntrySelected(compositeId: string) {
    const ids = compositeId.split(':', 2);
    const resultId = ids[0];
    const entryId = ids[1];
    const entry1: DebugEntry = _.find(this._sortedEntries, {resultId: resultId, id: entryId}) as DebugEntry;
    this.result = _.find(this.results, {id: resultId});
    this.debugSelected.emit(entry1);
  }
}
