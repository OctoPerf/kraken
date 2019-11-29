import {Component, Inject} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import {LocalStorageService} from 'projects/tools/src/lib/local-storage.service';

export interface ExecuteSimulationDialogData {
  simulationPackage: string;
  simulationClass: string;
  debug: boolean;
  atOnce: boolean;
}

@Component({
  selector: 'app-execute-simulation-dialog',
  templateUrl: './execute-simulation-dialog.component.html',
})
export class ExecuteSimulationDialogComponent {

  simulationForm: FormGroup;

  constructor(public dialogRef: MatDialogRef<ExecuteSimulationDialogComponent>,
              @Inject(MAT_DIALOG_DATA) public data: ExecuteSimulationDialogData,
              private fb: FormBuilder,
              private localStorage: LocalStorageService) {
    this.simulationForm = this.fb.group({
      simulationName: [data.simulationPackage + '.' + data.simulationClass, [
        Validators.required,
        Validators.pattern(/^(\w+\.)*\w+$/),
      ]],
      description: [data.simulationClass, [
        Validators.required,
        Validators.pattern(/^[\w\s]+$/)
      ]],
      javaOpts: [this.localStorage.getString('run-simulation-' + data.debug, ''), []],
    });
  }

  get simulationName() {
    return this.simulationForm.get('simulationName');
  }

  get description() {
    return this.simulationForm.get('description');
  }

  get javaOpts() {
    return this.simulationForm.get('javaOpts');
  }

  run() {
    const javaOpts = this.javaOpts.value || '';
    this.localStorage.set('run-simulation-' + this.data.debug, javaOpts);
    this.dialogRef.close({
      simulationName: this.simulationName.value,
      description: this.description.value,
      javaOpts,
    });
  }
}
