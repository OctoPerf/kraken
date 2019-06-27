import {Component} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-record-har-dialog',
  templateUrl: './import-har-dialog.component.html',
})
export class ImportHarDialogComponent {

  recordForm: FormGroup;

  constructor(public dialogRef: MatDialogRef<ImportHarDialogComponent>,
              private fb: FormBuilder) {
    this.recordForm = this.fb.group({
      simulationClass: ['', [
        Validators.required,
        Validators.pattern(/^[\w]+$/)
      ]],
      simulationPackage: ['', [
        Validators.required,
        Validators.pattern(/^(\w+\.)*\w+$/)
      ]],
    });
  }

  get simulationClass() {
    return this.recordForm.get('simulationClass');
  }

  get simulationPackage() {
    return this.recordForm.get('simulationPackage');
  }
}
