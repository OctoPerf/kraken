import {ComponentFixture, fakeAsync, TestBed, tick, waitForAsync} from '@angular/core/testing';

import {EnvironmentVariablesListComponent} from './environment-variables-list.component';
import {LocalStorageService} from 'projects/tools/src/lib/local-storage.service';
import {localStorageServiceSpy} from 'projects/tools/src/lib/local-storage.service.spec';
import {FormBuilder, FormGroup} from '@angular/forms';
import {ExecutionEnvironmentEntry} from 'projects/runtime/src/lib/entities/execution-environment-entry';
import SpyObj = jasmine.SpyObj;

describe('EnvironmentVariablesListComponent', () => {
  let component: EnvironmentVariablesListComponent;
  let fixture: ComponentFixture<EnvironmentVariablesListComponent>;
  let localStorage: SpyObj<LocalStorageService>;

  beforeEach(waitForAsync(() => {
    localStorage = localStorageServiceSpy();
    TestBed.configureTestingModule({
      declarations: [EnvironmentVariablesListComponent],
      providers: [
        {provide: LocalStorageService, useValue: localStorage},
        FormBuilder,
      ]
    })
      .overrideTemplate(EnvironmentVariablesListComponent, '')
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EnvironmentVariablesListComponent);
    component = fixture.componentInstance;
    component.formGroup = new FormGroup({});
    component.hostIds = ['hostId'];
    component.storageId = 'id';
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should init form', () => {
    localStorage.getItem.and.returnValue([{key: 'key', value: 'value', scope: null}, {
      key: 'key',
      value: 'value',
      scope: 'hostId'
    }]);
    component.ngOnInit();
    expect(component.entries).toEqual([new ExecutionEnvironmentEntry('', 'USER', 'key', 'value'),
      new ExecutionEnvironmentEntry('hostId', 'USER', 'key', 'value')]);
  });

  it('should addVariableIfEmpty', fakeAsync(() => {
    component.envKeyChildren = {
      last: {
        nativeElement: {
          focus: jasmine.createSpy('focus')
        }
      }
    } as any;
    component.addVariableIfEmpty();
    component.addVariableIfEmpty();
    expect(component.variables.length).toBe(1);
    expect(component.getKey(0).value).toBe('');
    expect(component.getValue(0).value).toBe('');
    tick(100);
    expect(component.envKeyChildren.last.nativeElement.focus).toHaveBeenCalled();
  }));


  it('should remove variable', () => {
    component.envKeyChildren = {
      last: {
        nativeElement: {
          focus: jasmine.createSpy('focus')
        }
      }
    } as any;
    component.addVariable();
    expect(component.variables.length).toBe(1);
    component.removeVariable(0);
    expect(component.variables.length).toBe(0);
  });
});
