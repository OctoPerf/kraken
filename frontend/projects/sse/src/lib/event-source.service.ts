import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import * as _ from 'lodash';
import {QueryParamsToStringPipe} from 'projects/tools/src/lib/query-params-to-string.pipe';
import {NativeEventSource, EventSourcePolyfill} from 'event-source-polyfill';

const EventSource = NativeEventSource || EventSourcePolyfill;

@Injectable({
  providedIn: 'root'
})
export class EventSourceService {

  constructor() {
  }

  newEventSource(path: string): EventSource {
    return new EventSource(path);
  }

  newObservable<R>(path: string,
                   options: { errorMessage?: string, params?: { [key in string]: string }, converter?: (data: string) => R }
                     = {}): Observable<R> {
    options = _.defaults(options, {
      errorMessage: '',
      converter: _.identity,
    });
    return new Observable(observer => {
      const eventSource = this.newEventSource(path + new QueryParamsToStringPipe().transform(options.params));
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
