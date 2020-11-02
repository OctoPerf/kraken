import {ComponentFixture, TestBed, waitForAsync} from '@angular/core/testing';

import {ImportHarDialogComponent} from 'projects/gatling/src/app/simulations/simulation-dialogs/import-har-dialog/import-har-dialog.component';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {dialogRefSpy} from 'projects/commons/src/lib/mock/material.mock.spec';
import {VendorsModule} from 'projects/vendors/src/lib/vendors.module';
import {ExecutionEnvironment} from 'projects/runtime/src/lib/entities/execution-environment';
import {ExecutionEnvironmentEntry} from 'projects/runtime/src/lib/entities/execution-environment-entry';
import SpyObj = jasmine.SpyObj;

describe('ImportHarDialogComponent', () => {
  let component: ImportHarDialogComponent;
  let fixture: ComponentFixture<ImportHarDialogComponent>;
  let dialogRef: SpyObj<MatDialogRef<any>>;

  beforeEach(waitForAsync(() => {
    dialogRef = dialogRefSpy();

    TestBed.configureTestingModule({
      imports: [VendorsModule],
      declarations: [ImportHarDialogComponent],
      providers: [
        {provide: MatDialogRef, useValue: dialogRef},
        {
          provide: MAT_DIALOG_DATA,
          useValue: {
            harPath: 'harPath'
          }
        },
      ]
    })
      .overrideTemplate(ImportHarDialogComponent, '')
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ImportHarDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should return simulationPackage', () => {
    expect(component.simulationPackage.value).toBe('');
  });

  it('should return simulationClass', () => {
    expect(component.simulationClass.value).toBe('');
  });

  it('should import', () => {
    component.hostsSelector = {
      hostId: 'local'
    } as any;
    component.import();
    expect(dialogRef.close).toHaveBeenCalledWith(new ExecutionEnvironment(
      'GATLING_RECORD',
      'Import har .',
      ['local'],
      [
        new ExecutionEnvironmentEntry('', 'FRONTEND', 'KRAKEN_GATLING_SIMULATION_CLASS_NAME', ''),
        new ExecutionEnvironmentEntry('', 'FRONTEND', 'KRAKEN_GATLING_SIMULATION_PACKAGE_NAME', ''),
        new ExecutionEnvironmentEntry('', 'FRONTEND', 'KRAKEN_GATLING_HAR_PATH_REMOTE', 'harPath'),
      ]));
  });

});
