import {Component, ElementRef, Input, OnInit, QueryList, ViewChildren} from '@angular/core';
import {FormArray, FormBuilder, FormGroup, Validators} from '@angular/forms';
import {ADD_ICON, DELETE_ICON} from 'projects/icon/src/lib/icons';
import {LocalStorageService} from 'projects/tools/src/lib/local-storage.service';
import * as _ from 'lodash';
import {ExecutionEnvironmentEntry} from 'projects/runtime/src/lib/entities/execution-environment-entry';

@Component({
  selector: 'lib-environment-variables-list',
  templateUrl: './environment-variables-list.component.html',
  styleUrls: ['./environment-variables-list.component.scss']
})
export class EnvironmentVariablesListComponent implements OnInit {

  private static readonly ID_PREFIX = 'environment-variables-list-';

  readonly addVariableIcon = ADD_ICON;
  readonly removeVariableIcon = DELETE_ICON;

  @Input() formGroup: FormGroup;
  @Input() hostIds: string[];
  @Input() storageId: string;
  envExpanded = false;

  @ViewChildren('key') envKeyChildren: QueryList<ElementRef>;

  constructor(private fb: FormBuilder,
              private localStorage: LocalStorageService) {
  }

  ngOnInit(): void {
    this.formGroup.addControl('variables', this.fb.array([]));
    const variables = this.variables;
    const entries = this.localStorage.getItem<ExecutionEnvironmentEntry[]>(EnvironmentVariablesListComponent.ID_PREFIX + this.storageId, []);
    _.forEach(entries, item => {
      variables.push(this.newVariableFormGroup(item.key, item.value, item.scope));
    });
    this.envExpanded = entries.length > 0;
  }

  get variables(): FormArray {
    return this.formGroup.get('variables') as FormArray;
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
    }, 100);
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

  get entries(): ExecutionEnvironmentEntry[] {
    const variables = this.variables;
    const entries: ExecutionEnvironmentEntry[] = variables.controls.map(control => {
      return new ExecutionEnvironmentEntry(control.get('scope').value || '',
        'USER',
        control.get('key').value,
        control.get('value').value
      );
    });
    this.saveEntries(entries);
    return entries;
  }

  private newVariableFormGroup(key = '', value = '', scope = '') {
    return this.fb.group(
      {
        key: [key, [Validators.required, Validators.pattern(/^\w+$/)]],
        value: [value, [Validators.required, Validators.pattern(/^\w+$/)]],
        scope: [scope, []],
      });
  }

  private saveEntries(entries: ExecutionEnvironmentEntry[]) {
    this.localStorage.setItem(EnvironmentVariablesListComponent.ID_PREFIX + this.storageId, entries);
  }

}
