import {Pipe, PipeTransform} from '@angular/core';
import {StorageNode} from 'projects/storage/src/lib/entities/storage-node';
import * as _ from 'lodash';
import {PathToParentPathPipe} from 'projects/tools/src/lib/path-to-parent-path.pipe';

@Pipe({
  name: 'storageNodeToParentPath'
})
export class StorageNodeToParentPathPipe implements PipeTransform {

  constructor(private pathToParentPathPipe: PathToParentPathPipe) {
  }

  transform(node: StorageNode, args?: any): string {
    return this.pathToParentPathPipe.transform(node.path);
  }

}
