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

  public sortedResults: Result[];
  public entry: DebugEntry;
  public result: Result;

  ngOnInit() {
    this._sortedEntries = _.sortBy(this.debugEntries, 'date');
    this.sortedResults = _.sortBy(this.results, 'startDate');
    _.reverse(this.sortedResults);
    this.resultSelected(this.resultId);
    this.debugEntrySelected(this.debugEntryId);
  }

  filter(resultId: string): DebugEntry[] {
    return _.filter(this._sortedEntries, {resultId});
  }

  resultSelected(resultId: string) {
    this.result = _.find(this.results, {id: resultId});
    this.entry = _.find(this._sortedEntries, {resultId: this.result.id}) as DebugEntry;
    this.debugSelected.emit(this.entry);
  }

  debugEntrySelected(entryId: string) {
    this.entry = _.find(this._sortedEntries, {resultId: this.result.id, id: entryId}) as DebugEntry;
    this.debugSelected.emit(this.entry);
  }
}
