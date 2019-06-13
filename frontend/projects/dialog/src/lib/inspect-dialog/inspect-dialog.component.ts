import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material';

export interface InspectDialogData {
  object: string;
  name: string;
}

@Component({
  selector: 'lib-inspect-dialog',
  templateUrl: './inspect-dialog.component.html',
  styleUrls: ['./inspect-dialog.component.scss']
})
export class InspectDialogComponent {

  constructor(public dialogRef: MatDialogRef<InspectDialogComponent>,
              @Inject(MAT_DIALOG_DATA) public data: InspectDialogData) {
  }
}
