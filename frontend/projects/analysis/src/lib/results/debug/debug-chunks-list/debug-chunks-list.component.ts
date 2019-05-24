import {Component, OnInit, ViewChild} from '@angular/core';
import {DebugChunksListService} from 'projects/analysis/src/lib/results/debug/debug-chunks-list/debug-chunks-list.service';
import {MatSort, MatTableDataSource} from '@angular/material';
import {DebugChunk} from 'projects/analysis/src/lib/entities/debug-chunk';
import {IconDynamic} from 'projects/icon/src/lib/icon-dynamic';
import {IconFa} from 'projects/icon/src/lib/icon-fa';
import {faCheckSquare} from '@fortawesome/free-solid-svg-icons/faCheckSquare';
import {faTimesCircle} from '@fortawesome/free-solid-svg-icons/faTimesCircle';
import {library} from '@fortawesome/fontawesome-svg-core';
import {ResultsListService} from 'projects/analysis/src/lib/results/results-list.service';
import {MENU_ICON} from 'projects/icon/src/lib/icons';
import {faNotEqual} from '@fortawesome/free-solid-svg-icons/faNotEqual';
import {StorageListService} from 'projects/storage/src/lib/storage-list.service';

library.add(faTimesCircle, faCheckSquare, faNotEqual);

@Component({
  selector: 'lib-debug-chunks-list',
  templateUrl: './debug-chunks-list.component.html',
  styleUrls: ['./debug-chunks-list.component.scss'],
  providers: [
    DebugChunksListService,
    StorageListService,
  ]
})
export class DebugChunksListComponent implements OnInit {

  readonly displayedColumns: string[] = ['requestStatus', 'requestName', 'date', 'contextualMenu'];
  readonly statusIcon = new IconDynamic(
    new IconFa(faTimesCircle, 'error'),
    {
      OK: new IconFa(faCheckSquare, 'success'),
    }
  );
  readonly menuIcon = MENU_ICON;
  readonly compareIcon = new IconFa(faNotEqual, 'success');

  dataSource: MatTableDataSource<DebugChunk>;
  @ViewChild(MatSort) sort: MatSort;

  constructor(public resultsList: ResultsListService,
              public debugResult: DebugChunksListService) {
  }

  ngOnInit() {
    this.debugResult.init();
    this.debugResult.valuesSubject.subscribe((chunks: DebugChunk[]) => {
      this.dataSource = new MatTableDataSource(chunks);
      this.dataSource.sort = this.sort;
    });
  }

}
