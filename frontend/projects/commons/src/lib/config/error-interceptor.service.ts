import {Injectable} from '@angular/core';
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Observable, throwError} from 'rxjs';
import {catchError} from 'rxjs/internal/operators';
import {RestServerError} from './rest-server-error';
import {EventBusService} from 'projects/event/src/lib/event-bus.service';
import {NotificationEvent} from 'projects/notification/src/lib/notification-event';
import {NotificationLevel} from 'projects/notification/src/lib/notification-level';
import {ErrorNotification} from 'projects/notification/src/lib/error-notification';
import {ConfigurationService} from 'projects/commons/src/lib/config/configuration.service';

@Injectable()
export class ErrorInterceptor implements HttpInterceptor {

  constructor(private eventBus: EventBusService,
              private configuration: ConfigurationService) {
  }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    if (request.url.match(this.configuration.errorsMatcherRegexp)) {
      return next.handle(request).pipe(catchError<any, RestServerError>((error) => {
        const restError = RestServerError.fromError(error);
        this.eventBus.publish(new NotificationEvent(
          new ErrorNotification(`${restError.title}: ${restError.message}`, NotificationLevel.ERROR, restError.trace)
        ));
        return throwError(restError);
      }));
    }
    return next.handle(request);
  }
}
