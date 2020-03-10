import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {ExecuteSimulationDialogComponent} from './execute-simulation-dialog.component';
import {VendorsModule} from 'projects/vendors/src/lib/vendors.module';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {dialogRefSpy} from 'projects/commons/src/lib/mock/material.mock.spec';
import {ExecutionEnvironment} from 'projects/runtime/src/lib/entities/execution-environment';
import SpyObj = jasmine.SpyObj;
import {ExecutionEnvironmentEntry} from 'projects/runtime/src/lib/entities/execution-environment-entry';

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
            type: 'GATLING_RUN',
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
      entries: [new ExecutionEnvironmentEntry('', 'USER', 'Foo', 'Bar')],
    } as any;
    component.hostsSelector = {
      hostIds: ['local']
    } as any;
    component.run();
    expect(dialogRef.close).toHaveBeenCalledWith(new ExecutionEnvironment(
      'GATLING_RUN',
      'description',
      ['local'],
      [new ExecutionEnvironmentEntry('', 'USER', 'Foo', 'Bar'),
        new ExecutionEnvironmentEntry('', 'FRONTEND', 'KRAKEN_GATLING_SIMULATION', 'simulationPackage.simulationClass')]
    ));
  });
});
