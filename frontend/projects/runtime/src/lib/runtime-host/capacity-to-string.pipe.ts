import {Pipe, PipeTransform} from '@angular/core';
import * as _ from 'lodash';

@Pipe({
  name: 'capacityToString'
})
export class CapacityToStringPipe implements PipeTransform {

  transform(capacity: { [key in string]: string }): string {
    return _.keys(capacity).map(key => `${key}=${capacity[key]}`).join(', ');
  }

}
