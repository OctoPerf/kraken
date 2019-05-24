import {Pipe, PipeTransform} from '@angular/core';
import * as moment from 'moment';

@Pipe({
  name: 'durationToString',
  pure: true,
})
export class DurationToStringPipe implements PipeTransform {

  transform(value: number, args?: any): any {
    return moment.duration(value).humanize();
  }

}
