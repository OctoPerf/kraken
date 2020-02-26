import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {AttachHostDialogComponent} from './attach-host-dialog.component';
import {ExecuteSimulationDialogComponent} from 'projects/gatling/src/app/simulations/simulation-dialogs/execute-simulation-dialog/execute-simulation-dialog.component';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {dialogRefSpy} from 'projects/commons/src/lib/mock/material.mock.spec';
import {VendorsModule} from 'projects/vendors/src/lib/vendors.module';
import {ExecutionContext} from 'projects/runtime/src/lib/entities/execution-context';
import SpyObj = jasmine.SpyObj;

describe('AttachHostDialogComponent', () => {
  let component: AttachHostDialogComponent;
  let fixture: ComponentFixture<AttachHostDialogComponent>;
  let dialogRef: SpyObj<MatDialogRef<AttachHostDialogComponent>>;

  beforeEach(async(() => {
    dialogRef = dialogRefSpy();
    TestBed.configureTestingModule({
      imports: [VendorsModule],
      declarations: [AttachHostDialogComponent],
      providers: [
        {
          provide: MAT_DIALOG_DATA,
          useValue: {
            title: 'title',
          }
        },
        {provide: MatDialogRef, useValue: dialogRef},
      ]
    })
      .overrideTemplate(ExecuteSimulationDialogComponent, '')
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AttachHostDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should return simulationName', () => {
    expect(component.hostId.value).toBe('');
  });


  it('should attach', () => {
    component.attach();
    expect(dialogRef.close).toHaveBeenCalledWith('');
  });
});
