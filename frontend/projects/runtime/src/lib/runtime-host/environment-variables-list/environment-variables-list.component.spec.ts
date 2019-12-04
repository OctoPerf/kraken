import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {EnvironmentVariablesListComponent} from './environment-variables-list.component';
import {RuntimeHostService} from 'projects/runtime/src/lib/runtime-host/runtime-host.service';
import {runtimeHostServiceSpy} from 'projects/runtime/src/lib/runtime-host/runtime-host.service.spec';
import {LocalStorageService} from 'projects/tools/src/lib/local-storage.service';
import {localStorageServiceSpy} from 'projects/tools/src/lib/local-storage.service.spec';
import {FormBuilder, FormGroup} from '@angular/forms';

describe('EnvironmentVariablesListComponent', () => {
  let component: EnvironmentVariablesListComponent;
  let fixture: ComponentFixture<EnvironmentVariablesListComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [EnvironmentVariablesListComponent],
      providers: [
        {provide: LocalStorageService, useValue: localStorageServiceSpy()},
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
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
