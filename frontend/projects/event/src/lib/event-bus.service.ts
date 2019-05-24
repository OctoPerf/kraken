import * as _ from 'lodash';
import {Injectable} from '@angular/core';
import {Observable, Subject} from 'rxjs';
import {filter} from 'rxjs/internal/operators';
import {BusEvent} from './bus-event';

@Injectable({
  providedIn: 'root'
})
export class EventBusService {
  private events: Subject<BusEvent>;

  constructor() {
    this.events = new Subject<BusEvent>();
  }

  publish(event: BusEvent): void {
    // console.log(event);
    this.events.next(event);
  }

  of<T extends BusEvent>(...channels: string[]): Observable<T> {
    return this.events.pipe(filter((event: T) => {
      return _.indexOf(channels, event.channel) !== -1;
    }));
  }
}
