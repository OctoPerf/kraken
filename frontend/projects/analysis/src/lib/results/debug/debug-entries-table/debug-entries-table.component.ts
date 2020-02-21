import {Component, OnInit, ViewChild} from '@angular/core';
import {MatSort} from '@angular/material/sort';
import {MatTableDataSource} from '@angular/material/table';
import {DebugEntry} from 'projects/analysis/src/lib/entities/debug-entry';
import {IconDynamic} from 'projects/icon/src/lib/icon-dynamic';
import {IconFa} from 'projects/icon/src/lib/icon-fa';
import {faCheckSquare} from '@fortawesome/free-solid-svg-icons/faCheckSquare';
import {faTimesCircle} from '@fortawesome/free-solid-svg-icons/faTimesCircle';
import {library} from '@fortawesome/fontawesome-svg-core';
import {MENU_ICON} from 'projects/icon/src/lib/icons';
import {faNotEqual} from '@fortawesome/free-solid-svg-icons/faNotEqual';
import {StorageListService} from 'projects/storage/src/lib/storage-list.service';
import {DebugEntriesTableService} from 'projects/analysis/src/lib/results/debug/debug-entries-table/debug-entries-table.service';

library.add(faTimesCircle, faCheckSquare, faNotEqual);

@Component({
  selector: 'lib-debug-entries-table',
  templateUrl: './debug-entries-table.component.html',
  styleUrls: ['./debug-entries-table.component.scss'],
  providers: [
    DebugEntriesTableService,
    StorageListService,
  ]
})
export class DebugEntriesTableComponent implements OnInit {

  readonly displayedColumns: string[] = ['requestStatus', 'requestName', 'date', 'contextualMenu'];
  readonly statusIcon = new IconDynamic(
    new IconFa(faTimesCircle, 'error'),
    {
      OK: new IconFa(faCheckSquare, 'success'),
    }
  );
  readonly menuIcon = MENU_ICON;
  readonly compareIcon = new IconFa(faNotEqual, 'success');
  readonly ID = 'debugs';

  dataSource: MatTableDataSource<DebugEntry> = new MatTableDataSource([]);
  @ViewChild(MatSort) sort: MatSort;

  constructor(public debugResult: DebugEntriesTableService,
  ) {
  }

  ngOnInit() {
    this.debugResult.init();
    this.debugResult.valuesSubject.subscribe((entries: DebugEntry[]) => {
      this.dataSource = new MatTableDataSource(entries);
      this.dataSource.sort = this.sort;
    });
  }

  public compareSelection() {
    this.debugResult.compare();
  }
}
