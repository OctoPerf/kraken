import {Component, Inject, ViewChild} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {TaskType} from 'projects/runtime/src/lib/entities/task-type';
import {DescriptionInputComponent} from 'projects/gatling/src/app/simulations/simulation-dialogs/description-input/description-input.component';
import {EnvironmentVariablesListComponent} from 'projects/runtime/src/lib/runtime-host/environment-variables-list/environment-variables-list.component';
import {ExecutionEnvironment} from 'projects/runtime/src/lib/entities/execution-environment';
import {HostsSelectorComponent} from 'projects/runtime/src/lib/runtime-host/hosts-selector/hosts-selector.component';
import {ExecutionEnvironmentEntry} from 'projects/runtime/src/lib/entities/execution-environment-entry';

export interface ExecuteSimulationDialogData {
  simulationPackage: string;
  simulationClass: string;
  type: TaskType;
  atOnce: boolean;
}

@Component({
  selector: 'app-execute-simulation-dialog',
  templateUrl: './execute-simulation-dialog.component.html',
})
export class ExecuteSimulationDialogComponent {

  simulationForm: FormGroup;

  @ViewChild('descriptionInput', {static: true})
  descriptionInput: DescriptionInputComponent;

  @ViewChild('envVarList', {static: true})
  envVarList: EnvironmentVariablesListComponent;

  @ViewChild('hostsSelector', {static: true})
  hostsSelector: HostsSelectorComponent;

  constructor(public dialogRef: MatDialogRef<ExecuteSimulationDialogComponent>,
              @Inject(MAT_DIALOG_DATA) public data: ExecuteSimulationDialogData,
              private fb: FormBuilder) {
    this.simulationForm = this.fb.group({
      simulationName: [data.simulationPackage + '.' + data.simulationClass, [
        Validators.required,
        Validators.pattern(/^(\w+\.)*\w+$/),
      ]],
    });
  }

  get simulationName() {
    return this.simulationForm.get('simulationName');
  }

  run() {
    const context = new ExecutionEnvironment(
      this.data.type,
      this.descriptionInput.description.value,
      this.hostsSelector.hostIds,
      this.envVarList.entries.concat(new ExecutionEnvironmentEntry('', 'FRONTEND', 'KRAKEN_GATLING_SIMULATION_NAME', this.simulationName.value))
    );
    this.dialogRef.close(context);
  }
}
