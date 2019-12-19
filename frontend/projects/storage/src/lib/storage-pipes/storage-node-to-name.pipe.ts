import {Pipe, PipeTransform} from '@angular/core';
import {StorageNode} from 'projects/storage/src/lib/entities/storage-node';
import {PathToNamePipe} from 'projects/tools/src/lib/path-to-name.pipe';

@Pipe({
  name: 'storageNodeToName'
})
export class StorageNodeToNamePipe implements PipeTransform {

  constructor(private pathToNamePipe: PathToNamePipe) {
  }

  transform(node: StorageNode, args?: any): string {
    return this.pathToNamePipe.transform(node.path);
  }

}
