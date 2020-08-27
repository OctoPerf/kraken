import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import * as _ from 'lodash';
import {StorageNode} from 'projects/storage/src/lib/entities/storage-node';
import {StorageNodeToIconPipe} from 'projects/storage/src/lib/storage-pipes/storage-node-to-icon.pipe';
import {StorageNodeToNamePipe} from 'projects/storage/src/lib/storage-pipes/storage-node-to-name.pipe';
import {StorageNodeToParentPathPipe} from 'projects/storage/src/lib/storage-pipes/storage-node-to-parent-path.pipe';
import {StorageNodeToExtPipe} from 'projects/storage/src/lib/storage-pipes/storage-node-to-ext.pipe';
import {NodeEventToNodePipe} from 'projects/storage/src/lib/storage-pipes/node-event-to-node.pipe';
import {StorageNodeToPredicatePipe} from 'projects/storage/src/lib/storage-pipes/storage-node-to-predicate.pipe';

export interface DeleteFilesDialogData {
  nodes: StorageNode[];
}

@Component({
  selector: 'lib-delete-files-dialog',
  templateUrl: './delete-files-dialog.component.html',
  styleUrls: ['./delete-files-dialog.component.scss'],
  providers: [
    StorageNodeToIconPipe,
    StorageNodeToExtPipe,
  ]
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
