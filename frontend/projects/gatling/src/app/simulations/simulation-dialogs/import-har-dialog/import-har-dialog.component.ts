import {Component, Inject, ViewChild} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {ExecutionEnvironment} from 'projects/runtime/src/lib/entities/execution-environment';
import {HostsSelectorComponent} from 'projects/runtime/src/lib/runtime-host/hosts-selector/hosts-selector.component';
import {ExecutionEnvironmentEntry} from 'projects/runtime/src/lib/entities/execution-environment-entry';

export interface ImportHarDialogComponentData {
  harPath: string;
}

@Component({
  selector: 'app-record-har-dialog',
  templateUrl: './import-har-dialog.component.html',
})
export class ImportHarDialogComponent {

  recordForm: FormGroup;

  @ViewChild('hostsSelector', {static: true})
  hostsSelector: HostsSelectorComponent;

  constructor(public dialogRef: MatDialogRef<ImportHarDialogComponent>,
              @Inject(MAT_DIALOG_DATA) public data: ImportHarDialogComponentData,
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

  import() {
    const hostId: string = this.hostsSelector.hostId;
    const hosts = {};
    hosts[hostId] = {};
    const context = new ExecutionEnvironment(
      'GATLING_RECORD',
      `Import har ${this.simulationPackage.value}.${this.simulationClass.value}`,
      [hostId],
      [
        new ExecutionEnvironmentEntry('', 'FRONTEND', 'KRAKEN_GATLING_SIMULATION_CLASSNAME', this.simulationClass.value),
        new ExecutionEnvironmentEntry('', 'FRONTEND', 'KRAKEN_GATLING_SIMULATION_PACKAGENAME', this.simulationPackage.value),
        new ExecutionEnvironmentEntry('', 'FRONTEND', 'KRAKEN_GATLING_HARPATH_REMOTE', this.data.harPath)
      ]
    );
    this.dialogRef.close(context);
  }
}
