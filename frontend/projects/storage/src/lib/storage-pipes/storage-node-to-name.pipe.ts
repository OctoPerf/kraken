import {Pipe, PipeTransform} from '@angular/core';
import {StorageNode} from 'projects/storage/src/lib/entities/storage-node';
import * as _ from 'lodash';

@Pipe({
  name: 'storageNodeToName'
})
export class StorageNodeToNamePipe implements PipeTransform {

  transform(node: StorageNode, args?: any): string {
    return _.last(_.split(node.path, '/'));
  }

}
