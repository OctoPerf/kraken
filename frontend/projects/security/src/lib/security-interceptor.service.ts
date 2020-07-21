import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Observable} from 'rxjs';
import {SecurityService} from 'projects/security/src/lib/security.service';
import {flatMap} from 'rxjs/operators';
import {Injectable} from '@angular/core';

@Injectable()
export class SecurityInterceptor implements HttpInterceptor {

  constructor(private security: SecurityService) {
  }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    if (this.security.authenticated) {
      return this.security.token.pipe(
        flatMap((token) => {
          const withAuth = request.clone({
            headers: request.headers.set('Authorization', `Bearer ${token}`)
          });
          return next.handle(withAuth);
        }));
    }
    return next.handle(request);
  }
}
