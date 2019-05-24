import {Component, Inject} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material';

export interface RunContainerData {
  name: string;
  config: string;
}

@Component({
  selector: 'lib-run-container-dialog',
  templateUrl: './run-container-dialog.component.html',
  styleUrls: ['./run-container-dialog.component.scss']
})
export class RunContainerDialogComponent {

  runForm: FormGroup;

  constructor(public dialogRef: MatDialogRef<RunContainerDialogComponent>,
              @Inject(MAT_DIALOG_DATA) public data: RunContainerData,
              private fb: FormBuilder) {
    this.runForm = this.fb.group({
      name: [data.name, [Validators.required]],
      config: [data.config, [Validators.required]],
    });
  }

  get name() {
    return this.runForm.get('name');
  }

  get config() {
    return this.runForm.get('config');
  }
}
