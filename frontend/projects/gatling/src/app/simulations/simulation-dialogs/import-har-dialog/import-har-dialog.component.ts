import {Component, Inject, ViewChild} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {ExecutionContext} from 'projects/runtime/src/lib/entities/execution-context';
import {ExecuteSimulationDialogData} from 'projects/gatling/src/app/simulations/simulation-dialogs/execute-simulation-dialog/execute-simulation-dialog.component';
import {EnvironmentVariablesListComponent} from 'projects/runtime/src/lib/runtime-host/environment-variables-list/environment-variables-list.component';
import {HostsSelectorComponent} from 'projects/runtime/src/lib/runtime-host/hosts-selector/hosts-selector.component';

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
    const context = new ExecutionContext(
      'RECORD',
      `Import har ${this.data.harPath}`,
      {
        KRAKEN_GATLING_SIMULATION_CLASS: this.simulationClass.value,
        KRAKEN_GATLING_SIMULATION_PACKAGE: this.simulationPackage.value,
        KRAKEN_GATLING_HAR_PATH_REMOTE: this.data.harPath
      },
      hosts
    );
    this.dialogRef.close(context);
  }
}
