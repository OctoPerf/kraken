import {Component, Inject, ViewChild} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {LocalStorageService} from 'projects/tools/src/lib/local-storage.service';
import {TaskType} from 'projects/runtime/src/lib/entities/task-type';
import {Host} from 'projects/runtime/src/lib/entities/host';
import {PrettyStringPipe} from 'projects/tools/src/lib/pretty-string.pipe';
import {DescriptionInputComponent} from 'projects/gatling/src/app/simulations/simulation-dialogs/description-input/description-input.component';
import {EnvironmentVariablesListComponent} from 'projects/runtime/src/lib/runtime-host/environment-variables-list/environment-variables-list.component';
import {ExecutionContext} from 'projects/runtime/src/lib/entities/execution-context';

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
    const context = new ExecutionContext(
      this.data.type,
      this.descriptionInput.description.value,
      this.envVarList.environment,
      this.envVarList.hosts
    );
    console.log(context);
    // TODO new ExecutionContext
    // this.dialogRef.close({
    //   simulationName: this.simulationName.value,
    //   // description: this.description.value,
    // });
  }
}
