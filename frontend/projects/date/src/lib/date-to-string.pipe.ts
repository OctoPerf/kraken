import {Pipe, PipeTransform} from '@angular/core';
import * as moment from 'moment';

@Pipe({
  name: 'dateToString',
  pure: true,
})
export class DateToStringPipe implements PipeTransform {

  transform(value: number | Date, args?: any): any {
    return moment(value).format('YYYY-MM-DD');
  }

}
