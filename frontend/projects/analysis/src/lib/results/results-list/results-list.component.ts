import {Component, OnInit, ViewChild} from '@angular/core';
import {GatlingResultService} from 'projects/analysis/src/lib/results/results-list/gatling-result.service';
import {Result} from 'projects/analysis/src/lib/entities/result';
import {MatSort, MatTableDataSource} from '@angular/material';
import {IconDynamic} from 'projects/icon/src/lib/icon-dynamic';
import {IconFa} from 'projects/icon/src/lib/icon-fa';
import {faQuestion} from '@fortawesome/free-solid-svg-icons/faQuestion';
import {faCheckSquare} from '@fortawesome/free-solid-svg-icons/faCheckSquare';
import {faExclamationTriangle} from '@fortawesome/free-solid-svg-icons/faExclamationTriangle';
import {faExclamationCircle} from '@fortawesome/free-solid-svg-icons/faExclamationCircle';
import {library} from '@fortawesome/fontawesome-svg-core';
import {faSync} from '@fortawesome/free-solid-svg-icons/faSync';
import {DEBUG_ICON, DELETE_ICON, MENU_ICON, PLAY_ICON} from 'projects/icon/src/lib/icons';
import {faChartLine} from '@fortawesome/free-solid-svg-icons/faChartLine';
import {faFileInvoice} from '@fortawesome/free-solid-svg-icons/faFileInvoice';
import {ResultsListService} from 'projects/analysis/src/lib/results/results-list.service';
import {faFileImport} from '@fortawesome/free-solid-svg-icons/faFileImport';

library.add(faQuestion, faCheckSquare, faExclamationTriangle, faExclamationCircle, faChartLine, faFileInvoice, faFileImport);

@Component({
  selector: 'lib-results-list',
  templateUrl: './results-list.component.html',
  styleUrls: ['./results-list.component.scss'],
  providers: [GatlingResultService]
})
export class ResultsListComponent implements OnInit {

  readonly displayedColumns: string[] = ['status', 'runDescription', 'startDate', 'contextualMenu'];
  readonly chartIcon = new IconFa(faChartLine, 'primary');
  readonly reportIcon = new IconFa(faFileInvoice);
  readonly deleteIcon = DELETE_ICON;
  readonly menuIcon = MENU_ICON;
  readonly statusIcon = new IconDynamic(
    new IconFa(faQuestion),
    {
      STARTING: new IconFa(faSync, 'muted', '', true),
      RUNNING: new IconFa(faSync, 'primary', '', true),
      COMPLETED: new IconFa(faCheckSquare, 'success'),
      CANCELED: new IconFa(faExclamationTriangle, 'warn'),
      FAILED: new IconFa(faExclamationCircle, 'error'),
    }
  );
  readonly typeIcon = new IconDynamic(
    new IconFa(faQuestion),
    {
      RUN: PLAY_ICON,
      DEBUG: DEBUG_ICON,
      HAR: new IconFa(faFileImport, 'muted')
    }
  );

  dataSource: MatTableDataSource<Result>;
  @ViewChild(MatSort) sort: MatSort;

  constructor(public gatling: GatlingResultService,
              public results: ResultsListService) {
  }

  ngOnInit() {
    this.results.init();
    this.results.valuesSubject.subscribe((resultsList) => {
      this.dataSource = new MatTableDataSource(resultsList);
      this.dataSource.sort = this.sort;
    });
  }

}
