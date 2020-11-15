import {ComponentFixture, TestBed, waitForAsync} from '@angular/core/testing';

import {LogsDialogComponent} from './logs-dialog.component';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {dialogRefSpy} from 'projects/commons/src/lib/mock/material.mock.spec';

describe('LogsDialogComponent', () => {
  let component: LogsDialogComponent;
  let fixture: ComponentFixture<LogsDialogComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [LogsDialogComponent],
      providers: [
        {provide: MAT_DIALOG_DATA, useValue: {title: 'name', logs: 'logs'}},
        {provide: MatDialogRef, useValue: dialogRefSpy()},
      ]
    })
      .overrideTemplate(LogsDialogComponent, '')
      .compileComponents();
  }));


  beforeEach(() => {
    fixture = TestBed.createComponent(LogsDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
