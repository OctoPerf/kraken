import { Pipe, PipeTransform } from '@angular/core';
import * as moment from 'moment';

@Pipe({
  name: 'dateTimeToString',
  pure: true,
})
export class DateTimeToStringPipe implements PipeTransform {

  transform(value: number | Date, args?: any): string {
    return moment(value).format('YYYY-MM-DD HH:mm:ss');
  }

}
