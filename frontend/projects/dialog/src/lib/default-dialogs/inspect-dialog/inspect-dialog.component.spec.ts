import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {InspectDialogComponent} from './inspect-dialog.component';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import {dialogRefSpy} from 'projects/commons/src/lib/mock/material.mock.spec';

describe('InspectDialogComponent', () => {
  let component: InspectDialogComponent;
  let fixture: ComponentFixture<InspectDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [InspectDialogComponent],
      providers: [
        {provide: MAT_DIALOG_DATA, useValue: {name: 'name', object: {}}},
        {provide: MatDialogRef, useValue: dialogRefSpy()},
      ]
    })
      .overrideTemplate(InspectDialogComponent, '')
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(InspectDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
