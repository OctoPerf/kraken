import {Pipe, PipeTransform} from '@angular/core';
import * as _ from 'lodash';

@Pipe({
  name: 'pathToName'
})
export class PathToNamePipe implements PipeTransform {

  transform(path: string, args?: any): string {
    return _.last(_.split(path, '/'));
  }

}
