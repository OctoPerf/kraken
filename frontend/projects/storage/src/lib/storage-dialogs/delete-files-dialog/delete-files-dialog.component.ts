import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material';
import * as _ from 'lodash';
import {StorageNode} from 'projects/storage/src/lib/entities/storage-node';

export interface DeleteFilesDialogData {
  nodes: StorageNode[];
}

@Component({
  selector: 'lib-delete-files-dialog',
  templateUrl: './delete-files-dialog.component.html',
  styleUrls: ['./delete-files-dialog.component.scss'],
})
export class DeleteFilesDialogComponent implements OnInit {

  public filesCount: number;
  public directoriesCount: number;

  constructor(public dialogRef: MatDialogRef<DeleteFilesDialogComponent>,
              @Inject(MAT_DIALOG_DATA) public data: DeleteFilesDialogData) {
  }

  ngOnInit() {
    this.directoriesCount = _.filter(this.data.nodes, (node: StorageNode) => node.type === 'DIRECTORY').length;
    this.filesCount = this.data.nodes.length - this.directoriesCount;
  }

}
