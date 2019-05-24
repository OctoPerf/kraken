import {Pipe, PipeTransform} from '@angular/core';
import * as moment from 'moment';

@Pipe({
  name: 'timeToStringMs',
  pure: true,
})
export class TimeToStringMsPipe implements PipeTransform {

  transform(value: number | Date, args?: any): any {
    return moment(value).format('HH:mm:ss:SSS');
  }

}
