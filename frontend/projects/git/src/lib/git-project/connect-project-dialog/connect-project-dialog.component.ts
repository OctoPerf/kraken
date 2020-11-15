import {Component, OnInit, ViewChild} from '@angular/core';
import {MatDialogRef} from '@angular/material/dialog';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {RepositoryUrlInputComponent} from 'projects/git/src/lib/git-project/repository-url-input/repository-url-input.component';
import {HELP_ICON} from 'projects/icon/src/lib/icons';

@Component({
  selector: 'lib-connect-project-modal',
  templateUrl: './connect-project-dialog.component.html',
  styleUrls: ['./connect-project-dialog.component.scss']
})
export class ConnectProjectDialogComponent {

  readonly helpIcon = HELP_ICON;

  connectForm: FormGroup;

  @ViewChild(RepositoryUrlInputComponent, {static: true})
  repositoryUrl: RepositoryUrlInputComponent;

  constructor(public dialogRef: MatDialogRef<ConnectProjectDialogComponent>,
              private fb: FormBuilder) {
    this.connectForm = this.fb.group({});
  }

  public connect(): void {
    this.dialogRef.close(this.repositoryUrl.repositoryUrl.value);
  }

}
