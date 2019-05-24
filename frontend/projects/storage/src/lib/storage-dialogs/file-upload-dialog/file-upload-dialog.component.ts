import {Component, ElementRef, Inject, ViewChild} from '@angular/core';
import {FileUploadService} from 'projects/storage/src/lib/storage-dialogs/file-upload-dialog/file-upload.service';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material';
import {forkJoin, Observable} from 'rxjs';
import {IconFa} from 'projects/icon/src/lib/icon-fa';
import {faFileUpload} from '@fortawesome/free-solid-svg-icons';
import {library} from '@fortawesome/fontawesome-svg-core';
import * as _ from 'lodash';

library.add(faFileUpload);

export interface FileUploadDialogData {
  endpoint: string;
  multiple: boolean;
  accept: string;
  title: string;
}

@Component({
  selector: 'lib-file-upload-dialog',
  templateUrl: './file-upload-dialog.component.html',
  providers: [FileUploadService]
})
export class FileUploadDialogComponent {

  readonly uploadIcon = new IconFa(faFileUpload);

  public files: File[] = [];
  public uploading = false;
  public progress: Observable<number>[];

  @ViewChild('file') _fileInput: ElementRef;

  constructor(private dialogRef: MatDialogRef<FileUploadDialogComponent>,
              @Inject(MAT_DIALOG_DATA) public data: FileUploadDialogData,
              private uploadService: FileUploadService) {
  }

  onFilesAdded() {
    const files = this._fileInput.nativeElement.files;
    for (let i = 0; i < files.length; i++) {
      this.files.push(files[i]);
    }
    this.uploading = true;
    this.dialogRef.disableClose = true;
    this.progress = this.uploadService.upload(this.files, this.data.endpoint);
    forkJoin(this.progress).subscribe(() => {
      this.dialogRef.close(_.map(files, 'name'));
    });
  }

  addFiles() {
    this._fileInput.nativeElement.click();
  }

}
