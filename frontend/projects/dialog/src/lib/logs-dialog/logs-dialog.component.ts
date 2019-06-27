import {Component, Inject} from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

export interface LogsDialogData {
  title: string;
  logs: string;
}

@Component({
  selector: 'lib-logs-dialog',
  templateUrl: './logs-dialog.component.html',
  styleUrls: ['./logs-dialog.component.scss']
})
export class LogsDialogComponent  {

  constructor(public dialogRef: MatDialogRef<LogsDialogComponent>,
              @Inject(MAT_DIALOG_DATA) public data: LogsDialogData) {
  }
}
