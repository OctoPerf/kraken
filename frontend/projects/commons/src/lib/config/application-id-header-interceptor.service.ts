import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Observable} from 'rxjs';
import {ConfigurationService} from 'projects/commons/src/lib/config/configuration.service';
import * as _ from 'lodash';
import {Injectable} from '@angular/core';

@Injectable()
export class ApplicationIdHeaderInterceptor implements HttpInterceptor {

  constructor(private configuration: ConfigurationService) {
  }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    if (_.includes(request.url, this.configuration.backendApiUrl)) {
      const withApp = request.clone({
        headers: request.headers.set('ApplicationId', this.configuration.applicationId)
      });
      return next.handle(withApp);
    }
    return next.handle(request);
  }
}
