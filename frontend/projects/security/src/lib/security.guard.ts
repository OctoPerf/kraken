import {Injectable} from '@angular/core';
import {
  ActivatedRouteSnapshot,
  CanActivate,
  CanLoad,
  Route,
  RouterStateSnapshot,
  UrlSegment,
  UrlTree
} from '@angular/router';
import {Observable, of} from 'rxjs';
import {SecurityService} from 'projects/security/src/lib/security.service';
import {map, mergeMap} from 'rxjs/operators';
import {SecurityConfigurationService} from 'projects/security/src/lib/security-configuration.service';
import * as _ from 'lodash';

@Injectable({
  providedIn: 'root'
})
export class SecurityGuard implements CanActivate, CanLoad {
  constructor(private security: SecurityService,
              private securityConfig: SecurityConfigurationService) {
  }

  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    return this.can();
  }

  canLoad(route: Route, segments: UrlSegment[]): Observable<boolean> | Promise<boolean> | boolean {
    return this.can();
  }

  private can(): Observable<boolean> {
    return this.security.init().pipe(
      mergeMap(authenticated => {
        if (!authenticated) {
          return this.security.login().pipe(map(() => false));
        } else if (!_.intersection(this.securityConfig.expectedRole, this.security.roles).length) {
          // TODO Check this
          return this.security.logout().pipe(map(() => false));
        }
        return of(true);
      })
    );
  }
}
