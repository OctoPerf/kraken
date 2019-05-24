import {Pipe, PipeTransform} from '@angular/core';
import * as moment from 'moment';

@Pipe({
  name: 'dateTimeToStringMs',
  pure: true,
})
export class DateTimeToStringMsPipe implements PipeTransform {

  transform(value: number | Date, args?: any): any {
    return moment(value).format('YYYY-MM-DD HH:mm:ss:SSS');
  }

}
