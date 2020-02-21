import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {DeleteFilesDialogComponent} from './delete-files-dialog.component';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import {dialogRefSpy} from 'projects/commons/src/lib/mock/material.mock.spec';
import {testStorageNodes} from 'projects/storage/src/lib/entities/storage-node.spec';

describe('DeleteFilesDialogComponent', () => {
  let component: DeleteFilesDialogComponent;
  let fixture: ComponentFixture<DeleteFilesDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [DeleteFilesDialogComponent],
      providers: [
        {provide: MAT_DIALOG_DATA, useValue: {nodes: testStorageNodes()}},
        {provide: MatDialogRef, useValue: dialogRefSpy()},
      ]
    })
      .overrideTemplate(DeleteFilesDialogComponent, '')
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DeleteFilesDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
    expect(component.directoriesCount).toBe(6);
    expect(component.filesCount).toBe(7);
  });
});
