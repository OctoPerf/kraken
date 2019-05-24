import {Pipe, PipeTransform} from '@angular/core';
import {StorageNode} from 'projects/storage/src/lib/entities/storage-node';
import * as _ from 'lodash';

@Pipe({
  name: 'storageNodeToExt'
})
export class StorageNodeToExtPipe implements PipeTransform {

  transform(node: StorageNode, args?: any): string {
    if (node.type === 'DIRECTORY') {
      return '';
    }
    return _.last(_.split(node.path, '.')).toLowerCase();
  }

}
