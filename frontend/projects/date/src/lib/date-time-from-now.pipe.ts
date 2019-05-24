import { Pipe, PipeTransform } from '@angular/core';
import * as moment from 'moment';

@Pipe({
  name: 'dateTimeFromNow',
  pure: true,
})
export class DateTimeFromNowPipe implements PipeTransform {

  transform(value: number | Date, args?: any): string {
    return moment(value).fromNow();
  }

}
