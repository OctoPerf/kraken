import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import * as _ from 'lodash';
import {EventSourcePolyfill} from 'event-source-polyfill';

@Injectable({
  providedIn: 'root'
})
export class EventSourceService {

  constructor() {
  }

  newEventSource(url: string, headers?: { [key in string]: string }): EventSource {
    return new EventSourcePolyfill(url, {headers});
  }

  newObservable<R>(url: string,
                   options: { errorMessage?: string, headers?: { [key in string]: string }, converter?: (data: string) => R }
                     = {}): Observable<R> {
    options = _.defaults(options, {
      errorMessage: '',
      converter: _.identity,
      headers: {}
    });
    return new Observable(observer => {
      const eventSource = this.newEventSource(url, options.headers);
      eventSource.onmessage = event => {
        observer.next(options.converter(event.data));
      };
      eventSource.onerror = () => {
        if (eventSource.readyState !== eventSource.CONNECTING) {
          observer.error(options.errorMessage);
        }
        eventSource.close();
        observer.complete();
      };
      return () => {
        eventSource.close();
      };
    });
  }
}
