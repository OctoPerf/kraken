import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material';
import {TaskType} from 'projects/runtime/src/lib/entities/task-type';
import {AbstractControl, FormBuilder, FormGroup, Validators} from '@angular/forms';
import {PrettyStringPipe} from 'projects/tools/src/lib/pretty-string.pipe';

export interface ExecutionDialogData {
  type: TaskType;
  simulationPackage: string;
  simulationClass: string;
  atOnce: boolean;
}

@Component({
  selector: 'lib-execution-dialog',
  templateUrl: './execution-dialog.component.html',
  styleUrls: ['./execution-dialog.component.scss']
})
export class ExecutionDialogComponent implements OnInit {

  type: TaskType;
  executionForm: FormGroup;

  constructor(public dialogRef: MatDialogRef<ExecutionDialogComponent>,
              @Inject(MAT_DIALOG_DATA) public data: ExecutionDialogData,
              private fb: FormBuilder,
              prettyStringPipe: PrettyStringPipe) {
    this.type = data.type;
    this.executionForm = this.fb.group({
      description: [`${prettyStringPipe.transform(data.type)} ${data.simulationPackage}.${data.simulationClass}`, [Validators.required]]
    });
    // this.command = data.command;
    // this.path = data.path;
    // const isShell = this.command.command[0] === Command.SHELL_0 &&
    //   this.command.command[1] === Command.SHELL_1 &&
    //   this.command.command.length === 3;
    //
    // this.selectedIndex = isShell ? ExecuteCommandDialogComponent.SHELL_INDEX : ExecuteCommandDialogComponent.ADVANCED_INDEX;
    //
    // this.shellForm = this.fb.group({
    //   shellCommand: [isShell ? this.command.command[2] : '', [
    //     Validators.required,
    //   ]],
    // });
    //
    // this.advancedForm = this.fb.group({
    //   commands: this.fb.array(_.map(this.command.command, (cmd: string) =>
    //     this.newCommandFormGroup(cmd)
    //   )),
    // });
    //
    // this.envForm = this.fb.group({
    //   variables: this.fb.array(_.map(this.command.environment, (value: string, key: string) =>
    //     this.newVariableFormGroup(key, value)
    //   )),
    // });
    // this.envExpanded = !!this.variables.controls.length;
  }

  get description(): AbstractControl {
    return this.executionForm.get('description');
  }

  ngOnInit() {
  }

}
