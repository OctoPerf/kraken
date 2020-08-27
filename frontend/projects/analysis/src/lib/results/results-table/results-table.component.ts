import {Component, OnInit, ViewChild} from '@angular/core';
import {GatlingResultService} from 'projects/analysis/src/lib/results/results-table/gatling-result.service';
import {Result} from 'projects/analysis/src/lib/entities/result';
import {MatSort} from '@angular/material/sort';
import {MatTableDataSource} from '@angular/material/table';
import {IconDynamic} from 'projects/icon/src/lib/icon-dynamic';
import {IconFa} from 'projects/icon/src/lib/icon-fa';
import {faQuestion} from '@fortawesome/free-solid-svg-icons/faQuestion';
import {faCheckSquare} from '@fortawesome/free-solid-svg-icons/faCheckSquare';
import {faExclamationTriangle} from '@fortawesome/free-solid-svg-icons/faExclamationTriangle';
import {faExclamationCircle} from '@fortawesome/free-solid-svg-icons/faExclamationCircle';
import {library} from '@fortawesome/fontawesome-svg-core';
import {DEBUG_ICON, DELETE_ICON, MENU_ICON, PLAY_ICON} from 'projects/icon/src/lib/icons';
import {faChartLine} from '@fortawesome/free-solid-svg-icons/faChartLine';
import {faFileInvoice} from '@fortawesome/free-solid-svg-icons/faFileInvoice';
import {ResultsTableService} from 'projects/analysis/src/lib/results/results-table/results-table.service';
import {faFileImport} from '@fortawesome/free-solid-svg-icons/faFileImport';
import {faCircleNotch} from '@fortawesome/free-solid-svg-icons/faCircleNotch';
import {ContextualMenuComponent} from 'projects/tree/src/lib/contextual-menu/contextual-menu.component';
import {OpenGatlingReportsDialogComponent} from 'projects/analysis/src/lib/analysis-dialogs/open-gatling-reports-dialog/open-gatling-reports-dialog.component';
import {DialogSize} from 'projects/dialog/src/lib/dialog-size';
import {MatPaginator} from '@angular/material/paginator';
import {DefaultDialogService} from 'projects/dialog/src/lib/default-dialogs/default-dialog.service';

library.add(faCircleNotch, faQuestion, faCheckSquare, faExclamationTriangle, faExclamationCircle, faChartLine, faFileInvoice, faFileImport);

@Component({
  selector: 'lib-results-table',
  templateUrl: './results-table.component.html',
  styleUrls: ['./results-table.component.scss'],
  providers: [GatlingResultService]
})
export class ResultsTableComponent implements OnInit {

  readonly ID = 'results';

  readonly displayedColumns: string[] = ['status', 'description', 'startDate', 'contextualMenu'];
  readonly chartIcon = new IconFa(faChartLine, 'primary');
  readonly reportIcon = new IconFa(faFileInvoice);
  readonly deleteIcon = DELETE_ICON;
  readonly menuIcon = MENU_ICON;
  readonly statusIcon = new IconDynamic(
    new IconFa(faQuestion),
    {
      STARTING: new IconFa(faCircleNotch, 'muted', '', true),
      RUNNING: new IconFa(faCircleNotch, 'primary', '', true),
      STOPPING: new IconFa(faCircleNotch, 'success', '', true),
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
  @ViewChild(MatSort, {static: true}) sort: MatSort;
  @ViewChild(MatPaginator, {static: true}) paginator: MatPaginator;
  @ViewChild('menu', {static: true}) menu: ContextualMenuComponent;

  constructor(public gatling: GatlingResultService,
              public results: ResultsTableService,
              private dialogs: DefaultDialogService) {
  }

  ngOnInit() {
    this.results.init();
    this.results.valuesSubject.subscribe((resultsList) => {
      this.dataSource = new MatTableDataSource(resultsList);
      this.dataSource.sort = this.sort;
      this.dataSource.paginator = this.paginator;
    });
  }

  openMenu(event: MouseEvent) {
    this.menu.open(event);
  }

  openGatlingReportsDialog(result: Result) {
    this.dialogs.open(OpenGatlingReportsDialogComponent, DialogSize.SIZE_SM, {result});
  }

  openGrafanaSelection(): boolean {
    if (this.gatling.canOpenGrafanaReport(this.results._selection.selection)) {
      this.gatling.openGrafanaReport(this.results._selection.selection);
      return true;
    }
    return false;
  }

  deleteSelection(force = false): boolean {
    if (this.gatling.canDeleteResult(this.results._selection.selection)) {
      this.gatling.deleteResult(this.results._selection.selection, force).subscribe();
      return true;
    }
    return false;
  }
}
