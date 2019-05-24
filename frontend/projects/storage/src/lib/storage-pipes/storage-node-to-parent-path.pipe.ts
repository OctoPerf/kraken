import {Pipe, PipeTransform} from '@angular/core';
import {StorageNode} from 'projects/storage/src/lib/entities/storage-node';
import * as _ from 'lodash';

@Pipe({
  name: 'storageNodeToParentPath'
})
export class StorageNodeToParentPathPipe implements PipeTransform {

  transform(node: StorageNode, args?: any): string {
    return _.join(_.dropRight(_.split(node.path, '/'), 1), '/');
  }

}
