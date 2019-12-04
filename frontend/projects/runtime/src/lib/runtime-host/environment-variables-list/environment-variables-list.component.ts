import {Component, ElementRef, Input, OnInit, QueryList, ViewChildren} from '@angular/core';
import {FormArray, FormBuilder, FormGroup, Validators} from '@angular/forms';
import {ADD_ICON, DELETE_ICON} from 'projects/icon/src/lib/icons';
import {LocalStorageService} from 'projects/tools/src/lib/local-storage.service';
import * as _ from 'lodash';

interface FlatVariable {
  key: string;
  value: string;
  scope: string;
}

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
    const flatVariables = this.localStorage.getItem<FlatVariable[]>(EnvironmentVariablesListComponent.ID_PREFIX + this.storageId, []);
    _.forEach(flatVariables, item => {
      variables.push(this.newVariableFormGroup(item.key, item.value, item.scope));
    });
    this.envExpanded = flatVariables.length > 0;
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

  get environment(): { [key in string]: string } {
    const flatVariables = this.flatVariables;
    this.saveFlatVariables(flatVariables);
    const global = _.filter(flatVariables, variable => !variable.scope);
    return this.flatVariablesToObject(global);
  }

  get hosts(): { [hostId in string]: { [key in string]: string } } {
    const flatVariables = this.flatVariables;
    this.saveFlatVariables(flatVariables);
    const hosts = {};
    _.forEach(this.hostIds, hostId => {
      const currentVariables = _.filter(flatVariables, variable => variable.scope === hostId);
      hosts[hostId] = this.flatVariablesToObject(currentVariables);
    });
    return hosts;
  }

  private newVariableFormGroup(key = '', value = '', scope = '') {
    return this.fb.group(
      {
        key: [key, [Validators.required, Validators.pattern(/^\w+$/)]],
        value: [value, [Validators.required, Validators.pattern(/^\w+$/)]],
        scope: [scope, []],
      });
  }

  private get flatVariables(): FlatVariable[] {
    const variables = this.variables;
    return variables.controls.map(control => {
      return {
        key: control.get('key').value,
        value: control.get('value').value,
        scope: control.get('scope').value,
      };
    });
  }

  private saveFlatVariables(flatVariables: FlatVariable[]) {
    this.localStorage.setItem(EnvironmentVariablesListComponent.ID_PREFIX + this.storageId, flatVariables);
  }

  private flatVariablesToObject(flatVariables: FlatVariable[]): { [key in string]: string } {
    return _.chain(flatVariables)
      .keyBy('key')
      .mapValues('value')
      .value();
  }
}
