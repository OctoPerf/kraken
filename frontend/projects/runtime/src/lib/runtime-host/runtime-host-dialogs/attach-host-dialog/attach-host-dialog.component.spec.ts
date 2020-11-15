import {ComponentFixture, TestBed, waitForAsync} from '@angular/core/testing';

import {AttachHostDialogComponent} from './attach-host-dialog.component';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {dialogRefSpy} from 'projects/commons/src/lib/mock/material.mock.spec';
import {VendorsModule} from 'projects/vendors/src/lib/vendors.module';
import {testPublicOwner} from 'projects/security/src/lib/entities/owner.spec';
import {testHost} from 'projects/runtime/src/lib/entities/host.spec';
import SpyObj = jasmine.SpyObj;

describe('AttachHostDialogComponent', () => {
  let component: AttachHostDialogComponent;
  let fixture: ComponentFixture<AttachHostDialogComponent>;
  let dialogRef: SpyObj<MatDialogRef<AttachHostDialogComponent>>;

  beforeEach(waitForAsync(() => {
    dialogRef = dialogRefSpy();
    TestBed.configureTestingModule({
      imports: [VendorsModule],
      declarations: [AttachHostDialogComponent],
      providers: [
        {
          provide: MAT_DIALOG_DATA,
          useValue: {
            title: 'title',
            host: testHost(),
          }
        },
        {provide: MatDialogRef, useValue: dialogRef},
      ]
    })
      .overrideTemplate(AttachHostDialogComponent, '')
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

  it('should return hostId', () => {
    expect(component.hostId.value).toBe(testHost().id);
  });

  it('should attach', () => {
    component.ownerSelector = {
      owner: testPublicOwner()
    } as any;
    component.attach();
    expect(dialogRef.close).toHaveBeenCalledWith(testHost());
  });
});
