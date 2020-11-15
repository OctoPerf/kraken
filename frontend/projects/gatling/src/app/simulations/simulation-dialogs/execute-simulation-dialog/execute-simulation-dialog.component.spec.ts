import {ComponentFixture, TestBed, waitForAsync} from '@angular/core/testing';

import {ExecuteSimulationDialogComponent} from './execute-simulation-dialog.component';
import {VendorsModule} from 'projects/vendors/src/lib/vendors.module';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {dialogRefSpy} from 'projects/commons/src/lib/mock/material.mock.spec';
import {ExecutionEnvironment} from 'projects/runtime/src/lib/entities/execution-environment';
import {ExecutionEnvironmentEntry} from 'projects/runtime/src/lib/entities/execution-environment-entry';
import SpyObj = jasmine.SpyObj;
import {LocalStorageService} from 'projects/tools/src/lib/local-storage.service';
import {localStorageServiceSpy} from 'projects/tools/src/lib/local-storage.service.spec';
import {durationInputComponentSpy} from 'projects/components/src/lib/duration-input/duration-input.component.spec';
import {DurationInputComponent} from 'projects/components/src/lib/duration-input/duration-input.component';

describe('ExecuteSimulationDialogComponent', () => {
  let component: ExecuteSimulationDialogComponent;
  let fixture: ComponentFixture<ExecuteSimulationDialogComponent>;
  let dialogRef: SpyObj<MatDialogRef<ExecuteSimulationDialogComponent>>;

  beforeEach(waitForAsync(() => {
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

  describe('should run', () => {
    beforeEach(() => {
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
      component.peakDuration = durationInputComponentSpy();
      component.rampUpDuration = durationInputComponentSpy();
    });

    it('custom setup', () => {
      component.customSetup.setValue(true);
      component.run();
      expect(dialogRef.close).toHaveBeenCalledWith(new ExecutionEnvironment(
        'GATLING_RUN',
        'description',
        ['local'],
        [new ExecutionEnvironmentEntry('', 'USER', 'Foo', 'Bar'),
          new ExecutionEnvironmentEntry('', 'FRONTEND', 'KRAKEN_GATLING_SIMULATION_NAME', 'simulationPackage.simulationClass'),
          new ExecutionEnvironmentEntry('', 'FRONTEND', 'KRAKEN_GATLING_SCENARIO_CUSTOM_SETUP', 'true'),
        ]
      ));
    });

    it('no custom setup', () => {
      component.customSetup.setValue(false);
      component.concurrentUsers.setValue(20);
      component.run();
      expect(dialogRef.close).toHaveBeenCalledWith(new ExecutionEnvironment(
        'GATLING_RUN',
        'description',
        ['local'],
        [new ExecutionEnvironmentEntry('', 'USER', 'Foo', 'Bar'),
          new ExecutionEnvironmentEntry('', 'FRONTEND', 'KRAKEN_GATLING_SIMULATION_NAME', 'simulationPackage.simulationClass'),
          new ExecutionEnvironmentEntry('', 'FRONTEND', 'KRAKEN_GATLING_SCENARIO_CUSTOM_SETUP', 'false'),
          new ExecutionEnvironmentEntry('', 'FRONTEND', 'KRAKEN_GATLING_SCENARIO_RAMP_UP_DURATION', 'PT5M'),
          new ExecutionEnvironmentEntry('', 'FRONTEND', 'KRAKEN_GATLING_SCENARIO_PEAK_DURATION', 'PT5M'),
          new ExecutionEnvironmentEntry('', 'FRONTEND', 'KRAKEN_GATLING_SCENARIO_CONCURRENT_USERS', '20'),
        ]
      ));
    });

  });

});
