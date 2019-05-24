import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import * as _ from 'lodash';
import {QueryParamsToStringPipe} from 'projects/tools/src/lib/query-params-to-string.pipe';
import {StringToolsService} from 'projects/tools/src/lib/string-tools.service';

@Injectable({
  providedIn: 'root'
})
export class EventSourceService {

  constructor(private paramsToString: QueryParamsToStringPipe) {
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
    return Observable.create(observer => {
      const eventSource = this.newEventSource(path + this.paramsToString.transform(options.params));
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
