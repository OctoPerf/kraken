import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {ExecuteSimulationDialogComponent} from './execute-simulation-dialog.component';
import {VendorsModule} from 'projects/vendors/src/lib/vendors.module';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import {dialogRefSpy} from 'projects/commons/src/lib/mock/material.mock.spec';
import SpyObj = jasmine.SpyObj;
import {LocalStorageService} from 'projects/tools/src/lib/local-storage.service';
import {localStorageServiceSpy} from 'projects/tools/src/lib/local-storage.service.spec';

describe('ExecuteSimulationDialogComponent', () => {
  let component: ExecuteSimulationDialogComponent;
  let fixture: ComponentFixture<ExecuteSimulationDialogComponent>;
  let dialogRef: SpyObj<MatDialogRef<ExecuteSimulationDialogComponent>>;

  beforeEach(async(() => {
    dialogRef = dialogRefSpy();
    TestBed.configureTestingModule({
      imports: [VendorsModule],
      declarations: [ExecuteSimulationDialogComponent],
      providers: [
        {
          provide: MAT_DIALOG_DATA,
          useValue: {simulationPackage: 'simulationPackage', simulationClass: 'simulationClass'}
        },
        {provide: MatDialogRef, useValue: dialogRef},
        {provide: LocalStorageService, useValue: localStorageServiceSpy()},
      ]
    })
      .overrideTemplate(ExecuteSimulationDialogComponent, '')
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ExecuteSimulationDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should return simulationName', () => {
    expect(component.simulationName.value).toBe('simulationPackage.simulationClass');
  });

  it('should return runDescription', () => {
    expect(component.runDescription.value).toBe('simulationClass');
  });

  it('should return javaOpts', () => {
    expect(component.javaOpts).toBeDefined();
  });

  it('should run', () => {
    component.run();
    expect(dialogRef.close).toHaveBeenCalledWith({
      simulationName: 'simulationPackage.simulationClass',
      runDescription: 'simulationClass',
      javaOpts: '',
    });
    expect(component.javaOpts).toBeDefined();
  });
});
