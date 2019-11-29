import {HttpHeader} from 'projects/analysis/src/lib/entities/http-header';

export interface DebugEntry {
  id: string;
  resultId: string;
  date: number;

  requestName: string;
  requestStatus: string;
  requestUrl: string;
  requestHeaders: HttpHeader[];
  requestCookies: string[];
  requestBodyFile: string;

  responseStatus: string;
  responseHeaders: HttpHeader[];
  responseBodyFile: string;

  session: string;
}
