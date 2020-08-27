import {Pipe, PipeTransform} from '@angular/core';
import {StorageNode} from 'projects/storage/src/lib/entities/storage-node';

@Pipe({
  name: 'storageNodeToPredicate',
})
export class StorageNodeToPredicatePipe implements PipeTransform {

  transform(node: StorageNode, args?: any): (current: StorageNode) => boolean {
    const childrenPath = node.path + '/';
    return (current: StorageNode) => {
      return current.path === node.path || (current.path.startsWith(childrenPath) && current.depth > node.depth);
    };
  }

}
