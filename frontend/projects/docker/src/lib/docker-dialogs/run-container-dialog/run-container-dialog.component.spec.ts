import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {RunContainerDialogComponent} from './run-container-dialog.component';
import {VendorsModule} from 'projects/vendors/src/lib/vendors.module';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material';
import {dialogRefSpy} from 'projects/commons/src/lib/mock/material.mock.spec';

describe('RunContainerDialogComponent', () => {
  let component: RunContainerDialogComponent;
  let fixture: ComponentFixture<RunContainerDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [VendorsModule],
      declarations: [RunContainerDialogComponent],
      providers: [
        {provide: MatDialogRef, useValue: dialogRefSpy()},
        {provide: MAT_DIALOG_DATA, useValue: {name: 'someName', config: 'someConfig'}},
      ]
    })
      .overrideTemplate(RunContainerDialogComponent, '')
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RunContainerDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
    expect(component.name).toBeTruthy();
    expect(component.config).toBeTruthy();
  });

  it('should return name', () => {
    expect(component.name.value).toBe('someName');
  });

  it('should return config', () => {
    expect(component.config.value).toBe('someConfig');
  });

});
