import {Pipe, PipeTransform} from '@angular/core';
import {DebugChunk} from 'projects/analysis/src/lib/entities/debug-chunk';
import {Observable, of, zip} from 'rxjs';
import {DateTimeToStringMsPipe} from 'projects/date/src/lib/date-time-to-string-ms.pipe';
import * as _ from 'lodash';
import {StorageService} from 'projects/storage/src/lib/storage.service';
import {DebugChunkToPathPipe} from 'projects/analysis/src/lib/results/debug/debug-pipes/debug-chunk-to-path.pipe';
import {StorageNode} from 'projects/storage/src/lib/entities/storage-node';
import {map} from 'rxjs/operators';

@Pipe({
  name: 'debugChunkToString'
})
export class DebugChunkToStringPipe implements PipeTransform {

  constructor(private dateToString: DateTimeToStringMsPipe,
              private storage: StorageService,
              private toPath: DebugChunkToPathPipe) {

  }

  transform(chunk: DebugChunk): Observable<string> {
    const str = (requestBody: string, responseBody: string) => `REQUEST ${chunk.requestName}
Date: ${this.dateToString.transform(chunk.date)}
Url: ${chunk.requestUrl}
Status: ${chunk.requestStatus}
Cookies:
\t${_.join(chunk.requestCookies, '\n\t')}
Headers:
\t${_.join(_.map(chunk.requestHeaders, header => header.key + ': ' + header.value), '\n\t')}
Body:
${requestBody}
================================================
RESPONSE
Status: ${chunk.responseStatus}
Headers:
\t${_.join(_.map(chunk.responseHeaders, header => header.key + ': ' + header.value), '\n\t')}
Body:
${responseBody}
================================================
SESSION
${chunk.session}
`;

    const getRequestBody = chunk.requestBodyFile ? this.storage.getContent({
      path: `${this.toPath.transform(chunk)}/${chunk.requestBodyFile}`
    } as StorageNode) : of('');

    const getResponseBody = chunk.responseBodyFile ? this.storage.getContent({
      path: `${this.toPath.transform(chunk)}/${chunk.responseBodyFile}`
    } as StorageNode) : of('');

    return zip(getRequestBody, getResponseBody).pipe(map((bodies: [string, string]) => str(bodies[0], bodies[1])));
  }

}
