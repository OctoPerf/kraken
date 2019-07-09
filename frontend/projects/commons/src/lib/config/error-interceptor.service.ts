import {Injectable} from '@angular/core';
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Observable, throwError} from 'rxjs';
import {catchError} from 'rxjs/internal/operators';
import {RestServerError} from './rest-server-error';
import {EventBusService} from 'projects/event/src/lib/event-bus.service';
import {NotificationEvent} from 'projects/notification/src/lib/notification-event';
import {NotificationLevel} from 'projects/notification/src/lib/notification-level';
import {ErrorNotification} from 'projects/notification/src/lib/error-notification';

@Injectable()
export class ErrorInterceptor implements HttpInterceptor {

  constructor(private eventBus: EventBusService) {
  }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(request).pipe(catchError<any, any>((error) => {
      const restError = RestServerError.fromError(error);
      this.eventBus.publish(new NotificationEvent(
        new ErrorNotification(`${restError.title}: ${restError.message}`, NotificationLevel.ERROR, restError.trace)
      ));
      return throwError(restError);
    }));
  }
}
