import {ComponentFixture, TestBed, waitForAsync} from '@angular/core/testing';

import {ConnectProjectDialogComponent} from 'projects/git/src/lib/git-project/connect-project-dialog/connect-project-dialog.component';
import {MatDialogRef} from '@angular/material/dialog';
import {dialogRefSpy} from 'projects/commons/src/lib/mock/material.mock.spec';
import {FormBuilder} from '@angular/forms';
import {repositoryUrlInputComponentSpy} from 'projects/git/src/lib/git-project/repository-url-input/repository-url-input.component.spec';
import SpyObj = jasmine.SpyObj;

describe('ConnectProjectDialogComponent', () => {
  let component: ConnectProjectDialogComponent;
  let fixture: ComponentFixture<ConnectProjectDialogComponent>;
  let dialogRef: SpyObj<MatDialogRef<ConnectProjectDialogComponent>>;

  beforeEach(waitForAsync(() => {
    dialogRef = dialogRefSpy();
    TestBed.configureTestingModule({
      declarations: [ConnectProjectDialogComponent],
      providers: [
        FormBuilder,
        {provide: MatDialogRef, useValue: dialogRef},
      ]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ConnectProjectDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should connect', () => {
    component.repositoryUrl = repositoryUrlInputComponentSpy();
    component.repositoryUrl.repositoryUrl.setValue('repositoryUrl');
    component.connect();
    expect(dialogRef.close).toHaveBeenCalledWith('repositoryUrl');
  });
});
