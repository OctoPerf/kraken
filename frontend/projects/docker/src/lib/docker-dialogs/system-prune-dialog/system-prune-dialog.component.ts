import {Component} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {MatDialogRef} from '@angular/material';

@Component({
  selector: 'lib-system-prune-dialog',
  templateUrl: './system-prune-dialog.component.html',
  styleUrls: ['./system-prune-dialog.component.scss']
})
export class SystemPruneDialogComponent {

  pruneForm: FormGroup;

  constructor(public dialogRef: MatDialogRef<SystemPruneDialogComponent>,
              private fb: FormBuilder) {
    this.pruneForm = this.fb.group({
      all: [false],
      volumes: [false],
    });
  }

  get all() {
    return this.pruneForm.get('all');
  }

  get volumes() {
    return this.pruneForm.get('volumes');
  }
}
