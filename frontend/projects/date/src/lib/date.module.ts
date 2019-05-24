import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {DateTimeToStringPipe} from './date-time-to-string.pipe';
import {DateTimeToStringMsPipe} from './date-time-to-string-ms.pipe';
import {DateTimeFromNowPipe} from './date-time-from-now.pipe';
import {DurationToStringPipe} from 'projects/date/src/lib/duration-to-string.pipe';
import {TimeToStringMsPipe} from 'projects/date/src/lib/time-to-string-ms.pipe';
import {DateToStringPipe} from 'projects/date/src/lib/date-to-string.pipe';

@NgModule({
  imports: [
    CommonModule
  ],
  declarations: [
    DateTimeToStringPipe,
    DateTimeToStringMsPipe,
    DateTimeFromNowPipe,
    DurationToStringPipe,
    TimeToStringMsPipe,
    DateToStringPipe,
  ],
  providers: [
    DateTimeToStringPipe,
    DateTimeToStringMsPipe,
    DateTimeFromNowPipe,
    DurationToStringPipe,
    TimeToStringMsPipe,
    DateToStringPipe,
  ],
  exports: [
    DateTimeToStringPipe,
    DateTimeToStringMsPipe,
    DateTimeFromNowPipe,
    DurationToStringPipe,
    TimeToStringMsPipe,
    DateToStringPipe,
  ]
})
export class DateModule {
}
