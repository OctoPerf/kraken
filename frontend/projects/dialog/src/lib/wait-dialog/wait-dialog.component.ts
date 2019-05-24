import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA} from '@angular/material';
import {WaitDialogProgress} from 'projects/dialog/src/lib/wait-dialog/wait-dialog-progress';

@Component({
  selector: 'lib-wait-dialog',
  templateUrl: './wait-dialog.component.html',
  styleUrls: ['./wait-dialog.component.scss']
})
export class WaitDialogComponent {

  private _progress: WaitDialogProgress;

  constructor(@Inject(MAT_DIALOG_DATA) progress: WaitDialogProgress) {
    this.progress = progress;
  }

  set progress(progress: WaitDialogProgress) {
    this._progress = progress;
  }

  get progress(): WaitDialogProgress {
    return this._progress;
  }
}
