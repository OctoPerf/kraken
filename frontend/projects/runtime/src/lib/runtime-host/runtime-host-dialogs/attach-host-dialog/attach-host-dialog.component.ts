import {Component, Inject} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';

export interface AttachHostDialogData {
  title: string;
}

@Component({
  selector: 'lib-attach-host-dialog',
  templateUrl: './attach-host-dialog.component.html'
})
export class AttachHostDialogComponent {

  hostForm: FormGroup;

  constructor(public dialogRef: MatDialogRef<AttachHostDialogComponent>,
              @Inject(MAT_DIALOG_DATA) public data: AttachHostDialogData,
              private fb: FormBuilder) {
    this.hostForm = this.fb.group({
      hostId: ['', [
        Validators.required,
        Validators.maxLength(63),
        Validators.minLength(4),
        Validators.pattern(/^[a-zA-Z0-9]+[a-zA-Z0-9\-_\.]*[a-zA-Z0-9]+$/),
      ]],
    });
  }

  get hostId() {
    return this.hostForm.get('hostId');
  }

  attach() {
    this.dialogRef.close(this.hostId.value);
  }

}
