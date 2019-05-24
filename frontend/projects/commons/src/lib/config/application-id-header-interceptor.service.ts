import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Observable} from 'rxjs';
import {ConfigurationService} from 'projects/commons/src/lib/config/configuration.service';
import * as _ from 'lodash';

export class ApplicationIdHeaderInterceptor implements HttpInterceptor {

  constructor(private configuration: ConfigurationService,
              private url: () => string) {
  }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    if (_.includes(request.url, this.url())) {
      const withApp = request.clone({
        headers: request.headers.set('ApplicationId', this.configuration.applicationId)
      });
      return next.handle(withApp);
    }
    return next.handle(request);
  }
}
