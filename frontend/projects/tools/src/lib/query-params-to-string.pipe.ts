import {Pipe, PipeTransform} from '@angular/core';
import * as _ from 'lodash';

@Pipe({
  name: 'queryParamsToString'
})
export class QueryParamsToStringPipe implements PipeTransform {

  transform(value?: { [key in string]: string }): string {
    if (!value || _.isEmpty(value)) {
      return '';
    }
    return '?' + _.join(_.map(value, (val, key) => `${key}=${val}`), '&');
  }

}
