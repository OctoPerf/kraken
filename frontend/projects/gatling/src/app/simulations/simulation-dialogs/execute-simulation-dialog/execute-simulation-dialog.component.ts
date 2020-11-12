import {Component, Inject, ViewChild} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {TaskType} from 'projects/runtime/src/lib/entities/task-type';
import {DescriptionInputComponent} from 'projects/gatling/src/app/simulations/simulation-dialogs/description-input/description-input.component';
import {EnvironmentVariablesListComponent} from 'projects/runtime/src/lib/runtime-host/environment-variables-list/environment-variables-list.component';
import {ExecutionEnvironment} from 'projects/runtime/src/lib/entities/execution-environment';
import {HostsSelectorComponent} from 'projects/runtime/src/lib/runtime-host/hosts-selector/hosts-selector.component';
import {ExecutionEnvironmentEntry} from 'projects/runtime/src/lib/entities/execution-environment-entry';
import {LocalStorageService} from 'projects/tools/src/lib/local-storage.service';
import {DurationInputComponent} from 'projects/components/src/lib/duration-input/duration-input.component';

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

  private static readonly CONCURRENT_USERS_ID = '-concurrent-users';
  private static readonly CUSTOM_SETUP_ID = '-custom-setup';

  simulationForm: FormGroup;
  storageId: string;

  @ViewChild('descriptionInput', {static: true})
  descriptionInput: DescriptionInputComponent;
  @ViewChild('envVarList', {static: true})
  envVarList: EnvironmentVariablesListComponent;
  @ViewChild('hostsSelector', {static: true})
  hostsSelector: HostsSelectorComponent;
  @ViewChild('rampUpDuration')
  rampUpDuration: DurationInputComponent;
  @ViewChild('peakDuration')
  peakDuration: DurationInputComponent;

  constructor(public dialogRef: MatDialogRef<ExecuteSimulationDialogComponent>,
              @Inject(MAT_DIALOG_DATA) public data: ExecuteSimulationDialogData,
              private fb: FormBuilder,
              private localStorage: LocalStorageService) {
    this.storageId = data.type;
    this.simulationForm = this.fb.group({
      simulationName: [data.simulationPackage + '.' + data.simulationClass, [
        Validators.required,
        Validators.pattern(/^(\w+\.)*\w+$/),
      ]],
      concurrentUsers: [localStorage.getNumber(this.storageId + ExecuteSimulationDialogComponent.CONCURRENT_USERS_ID, 10), [
        Validators.required,
        Validators.min(1),
      ]],
      customSetup: [localStorage.getBoolean(this.storageId + '-custom-setup', false), [
        Validators.required,
      ]],
    });
  }

  get simulationName() {
    return this.simulationForm.get('simulationName');
  }

  get concurrentUsers() {
    return this.simulationForm.get('concurrentUsers');
  }

  get customSetup() {
    return this.simulationForm.get('customSetup');
  }

  run() {
    this.localStorage.set(this.storageId + ExecuteSimulationDialogComponent.CONCURRENT_USERS_ID, this.concurrentUsers.value);
    this.localStorage.set(this.storageId + ExecuteSimulationDialogComponent.CUSTOM_SETUP_ID, this.customSetup.value);

    const setupEntries = [];
    const isCustomSetup = this.customSetup.value;
    setupEntries.push(new ExecutionEnvironmentEntry('', 'FRONTEND', 'KRAKEN_GATLING_SCENARIO_CUSTOM_SETUP', '' + isCustomSetup));
    if (!isCustomSetup && this.data.type === 'GATLING_RUN') {
      setupEntries.push(new ExecutionEnvironmentEntry('', 'FRONTEND', 'KRAKEN_GATLING_SCENARIO_RAMP_UP_DURATION', this.rampUpDuration.asString));
      setupEntries.push(new ExecutionEnvironmentEntry('', 'FRONTEND', 'KRAKEN_GATLING_SCENARIO_PEAK_DURATION', this.peakDuration.asString));
      setupEntries.push(new ExecutionEnvironmentEntry('', 'FRONTEND', 'KRAKEN_GATLING_SCENARIO_CONCURRENT_USERS', '' + this.concurrentUsers.value));
    }

    const context = new ExecutionEnvironment(
      this.data.type,
      this.descriptionInput.description.value,
      this.hostsSelector.hostIds,
      this.envVarList.entries
        .concat(new ExecutionEnvironmentEntry('', 'FRONTEND', 'KRAKEN_GATLING_SIMULATION_NAME', this.simulationName.value))
        .concat(setupEntries)
    );
    this.dialogRef.close(context);
  }
}
