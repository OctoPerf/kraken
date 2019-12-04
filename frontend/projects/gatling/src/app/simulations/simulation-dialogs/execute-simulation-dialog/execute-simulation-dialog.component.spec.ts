import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {ExecuteSimulationDialogComponent} from './execute-simulation-dialog.component';
import {VendorsModule} from 'projects/vendors/src/lib/vendors.module';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {dialogRefSpy} from 'projects/commons/src/lib/mock/material.mock.spec';
import {ExecutionContext} from 'projects/runtime/src/lib/entities/execution-context';
import SpyObj = jasmine.SpyObj;

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
          useValue: {
            simulationPackage: 'simulationPackage',
            simulationClass: 'simulationClass',
            type: 'RUN',
            atOnce: true,
          }
        },
        {provide: MatDialogRef, useValue: dialogRef},
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


  it('should run', () => {
    component.descriptionInput = {
      description: {
        value: 'description'
      }
    } as any;
    component.envVarList = {
      environment: {FOO: 'BAR'},
      hosts: {
        'local': {}
      }
    } as any;
    component.run();
    expect(dialogRef.close).toHaveBeenCalledWith(new ExecutionContext(
      'RUN',
      'description',
      {FOO: 'BAR', KRAKEN_GATLING_SIMULATION: 'simulationPackage.simulationClass'},
      {
        'local': {}
      }
    ));
  });
});
