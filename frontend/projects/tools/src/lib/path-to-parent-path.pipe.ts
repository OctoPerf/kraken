import {Pipe, PipeTransform} from '@angular/core';
import * as _ from 'lodash';

@Pipe({
  name: 'pathToParentPath'
})
export class PathToParentPathPipe implements PipeTransform {

  transform(path: string, args?: any): string {
    return _.join(_.dropRight(_.split(path, '/'), 1), '/');
  }

}
