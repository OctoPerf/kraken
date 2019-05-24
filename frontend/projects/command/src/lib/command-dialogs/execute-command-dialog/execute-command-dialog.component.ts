import {Component, ElementRef, Inject, OnInit, QueryList, ViewChildren} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material';
import {FormArray, FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Command} from 'projects/command/src/lib/entities/command';
import * as _ from 'lodash';
import {IconFa} from 'projects/icon/src/lib/icon-fa';
import {faPlus} from '@fortawesome/free-solid-svg-icons/faPlus';
import {faTrash} from '@fortawesome/free-solid-svg-icons';
import {library} from '@fortawesome/fontawesome-svg-core';

library.add(faPlus, faTrash);

@Component({
  selector: 'lib-execute-command-dialog',
  templateUrl: './execute-command-dialog.component.html',
  styleUrls: ['./execute-command-dialog.component.scss']
})
export class ExecuteCommandDialogComponent {

  static readonly SHELL_INDEX = 0;
  static readonly ADVANCED_INDEX = 1;

  readonly addVariableIcon = new IconFa(faPlus, 'success');
  readonly removeVariableIcon = new IconFa(faTrash, 'error');
  readonly addCommandIcon = new IconFa(faPlus, 'success');
  readonly removeCommandIcon = new IconFa(faTrash, 'error');

  shellForm: FormGroup;
  advancedForm: FormGroup;
  envForm: FormGroup;
  envExpanded: boolean;
  selectedIndex: number;

  @ViewChildren('key') envKeyChildren: QueryList<ElementRef>;
  @ViewChildren('cmd') commandChildren: QueryList<ElementRef>;

  public command: Command;
  public path: string;

  constructor(public dialogRef: MatDialogRef<ExecuteCommandDialogComponent>,
              @Inject(MAT_DIALOG_DATA) data: { command: Command, path: string },
              private fb: FormBuilder) {
    this.command = data.command;
    this.path = data.path;
    const isShell = this.command.command[0] === Command.SHELL_0 &&
      this.command.command[1] === Command.SHELL_1 &&
      this.command.command.length === 3;

    this.selectedIndex = isShell ? ExecuteCommandDialogComponent.SHELL_INDEX : ExecuteCommandDialogComponent.ADVANCED_INDEX;

    this.shellForm = this.fb.group({
      shellCommand: [isShell ? this.command.command[2] : '', [
        Validators.required,
      ]],
    });

    this.advancedForm = this.fb.group({
      commands: this.fb.array(_.map(this.command.command, (cmd: string) =>
        this.newCommandFormGroup(cmd)
      )),
    });

    this.envForm = this.fb.group({
      variables: this.fb.array(_.map(this.command.environment, (value: string, key: string) =>
        this.newVariableFormGroup(key, value)
      )),
    });
    this.envExpanded = !!this.variables.controls.length;
  }

  execute() {
    const env: { [key in string]: string } = {};
    for (let i = 0; i < this.variables.controls.length; i++) {
      env[this.getKey(i).value] = this.getValue(i).value;
    }
    let command: string[];
    if (this.selectedIndex === ExecuteCommandDialogComponent.SHELL_INDEX) {
      command = ['/bin/sh', '-c', this.shellCommand.value];
    } else {
      command = this.commands.getRawValue();
    }
    this.dialogRef.close(new Command(command, env));
  }

  isValid(): boolean {
    return this.envForm.valid
      && (this.shellForm.valid || this.selectedIndex === ExecuteCommandDialogComponent.ADVANCED_INDEX)
      && (this.advancedForm.valid || this.selectedIndex === ExecuteCommandDialogComponent.SHELL_INDEX);
  }

  // Shell

  get shellCommand() {
    return this.shellForm.get('shellCommand');
  }

  // Advanced

  get commands(): FormArray {
    return this.advancedForm.get('commands') as FormArray;
  }

  private newCommandFormGroup(cmd = '') {
    return this.fb.control(cmd, [Validators.required]);
  }

  addCommand() {
    this.commands.push(this.newCommandFormGroup());
    this.focusOnCommand();
  }

  focusOnCommand() {
    setTimeout(() => {
      this.commandChildren.last.nativeElement.focus();
    }, 1);
  }

  removeCommand(i: number) {
    this.commands.removeAt(i);
  }

  getCommand(i: number) {
    return this.commands.controls[i];
  }

  // Variables

  get variables(): FormArray {
    return this.envForm.get('variables') as FormArray;
  }

  addVariableIfEmpty() {
    if (!this.variables.controls.length) {
      this.addVariable();
    }
  }

  addVariable() {
    this.variables.push(this.newVariableFormGroup());
    this.focusOnVariable();
  }

  focusOnVariable() {
    setTimeout(() => {
      this.envKeyChildren.last.nativeElement.focus();
    }, 1);
  }

  removeVariable(i: number) {
    this.variables.removeAt(i);
  }

  getKey(i: number) {
    return this.variables.controls[i].get('key');
  }

  getValue(i: number) {
    return this.variables.controls[i].get('value');
  }

  private newVariableFormGroup(key = '', value = '') {
    return this.fb.group(
      {
        key: [key, [Validators.required]],
        value: [value, [Validators.required]],
      });
  }

}
