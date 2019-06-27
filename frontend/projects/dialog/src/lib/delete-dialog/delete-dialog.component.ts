import {Component, Inject} from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import {HelpPageId} from 'projects/help/src/lib/help-panel/help-pages';

export interface DeleteDialogData {
  name: string;
  items: string[];
  helpPageId?: HelpPageId;
}

@Component({
  selector: 'lib-delete-dialog',
  templateUrl: './delete-dialog.component.html',
  styleUrls: ['./delete-dialog.component.scss']
})
export class DeleteDialogComponent {

  constructor(public dialogRef: MatDialogRef<DeleteDialogComponent>,
              @Inject(MAT_DIALOG_DATA) public data: DeleteDialogData) {
  }
}
