import {Pipe, PipeTransform} from '@angular/core';
import {DebugEntry} from 'projects/analysis/src/lib/entities/debug-entry';
import {Observable, of, zip} from 'rxjs';
import {DateTimeToStringMsPipe} from 'projects/date/src/lib/date-time-to-string-ms.pipe';
import * as _ from 'lodash';
import {StorageService} from 'projects/storage/src/lib/storage.service';
import {StorageNode} from 'projects/storage/src/lib/entities/storage-node';
import {map} from 'rxjs/operators';
import {DebugEntryToPathPipe} from 'projects/analysis/src/lib/results/debug/debug-pipes/debug-entry-to-path.pipe';

@Pipe({
  name: 'debugEntryToString'
})
export class DebugEntryToStringPipe implements PipeTransform {

  constructor(private dateToString: DateTimeToStringMsPipe,
              private storage: StorageService,
              private toPath: DebugEntryToPathPipe) {

  }

  transform(entry: DebugEntry): Observable<string> {
    const str = (requestBody: string, responseBody: string) => `REQUEST ${entry.requestName}
Date: ${this.dateToString.transform(entry.date)}
Url: ${entry.requestUrl}
Status: ${entry.requestStatus}
Cookies:
\t${_.join(entry.requestCookies, '\n\t')}
Headers:
\t${_.join(_.map(entry.requestHeaders, header => header.key + ': ' + header.value), '\n\t')}
Body:
${requestBody}
================================================
RESPONSE
Status: ${entry.responseStatus}
Headers:
\t${_.join(_.map(entry.responseHeaders, header => header.key + ': ' + header.value), '\n\t')}
Body:
${responseBody}
================================================
SESSION
${entry.session}
`;

    const getRequestBody = entry.requestBodyFile ? this.storage.getContent({
      path: `${this.toPath.transform(entry)}/${entry.requestBodyFile}`
    } as StorageNode) : of('');

    const getResponseBody = entry.responseBodyFile ? this.storage.getContent({
      path: `${this.toPath.transform(entry)}/${entry.responseBodyFile}`
    } as StorageNode) : of('');

    return zip(getRequestBody, getResponseBody).pipe(map((bodies: [string, string]) => str(bodies[0], bodies[1])));
  }

}
