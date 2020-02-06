import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {DeleteFilesDialogData} from 'projects/storage/src/lib/storage-dialogs/delete-files-dialog/delete-files-dialog.component';
import {Result} from 'projects/analysis/src/lib/entities/result';
import {Observable, of} from 'rxjs';
import {StorageNode} from 'projects/storage/src/lib/entities/storage-node';
import {catchError} from 'rxjs/operators';
import {StorageService} from 'projects/storage/src/lib/storage.service';
import {AnalysisConfigurationService} from 'projects/analysis/src/lib/analysis-configuration.service';
import {WindowService} from 'projects/tools/src/lib/window.service';
import {IconFa} from 'projects/icon/src/lib/icon-fa';
import {faFileInvoice} from '@fortawesome/free-solid-svg-icons/faFileInvoice';
import {library} from '@fortawesome/fontawesome-svg-core';

library.add(faFileInvoice);

@Component({
  selector: 'lib-open-gatling-reports-dialog',
  templateUrl: './open-gatling-reports-dialog.component.html',
  styleUrls: ['./open-gatling-reports-dialog.component.scss']
})
export class OpenGatlingReportsDialogComponent implements OnInit {

  public loading = true;
  public reports: StorageNode[] = [];
  readonly reportIcon = new IconFa(faFileInvoice);

  constructor(public dialogRef: MatDialogRef<OpenGatlingReportsDialogComponent>,
              @Inject(MAT_DIALOG_DATA) public data: { result: Result },
              private storage: StorageService,
              private analysisConfiguration: AnalysisConfigurationService,
              private window: WindowService) {
  }

  ngOnInit() {
    const rootNode = this.analysisConfiguration.analysisRootNode;
    const path = `${rootNode.path}/${this.data.result.id}/groups/`;
    this.storage.find(path, 'index\.html')
      .pipe(catchError(err => of([])))
      .subscribe(reports => {
        this.reports = reports;
        this.loading = false;
      });
  }

  openGatlingReport(reportNode: StorageNode) {
    this.window.open(of(this.analysisConfiguration.staticApiUrl('/' + reportNode.path)));
  }

}
