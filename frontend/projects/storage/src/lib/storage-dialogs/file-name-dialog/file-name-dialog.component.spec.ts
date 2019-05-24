import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {FileNameDialogComponent} from './file-name-dialog.component';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material';
import {dialogRefSpy} from 'projects/commons/src/lib/mock/material.mock.spec';
import {VendorsModule} from 'projects/vendors/src/lib/vendors.module';

describe('FileNameDialogComponent', () => {
  let component: FileNameDialogComponent;
  let fixture: ComponentFixture<FileNameDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [VendorsModule],
      declarations: [FileNameDialogComponent],
      providers: [
        {provide: MAT_DIALOG_DATA, useValue: {title: 'title', name: 'name'}},
        {provide: MatDialogRef, useValue: dialogRefSpy()},
      ]
    })
      .overrideTemplate(FileNameDialogComponent, '')
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FileNameDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
    expect(component.fileName.value).toBe('name');
  });
});
