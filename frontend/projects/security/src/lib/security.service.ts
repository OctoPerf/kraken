import {Injectable} from '@angular/core';
import {SecurityConfigurationService} from 'projects/security/src/lib/security-configuration.service';
import {from, Observable, of} from 'rxjs';
import * as kc from 'keycloak-js';
import {flatMap, map, tap} from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class SecurityService {

  private kcInstance: kc.KeycloakInstance;

  constructor(private configuration: SecurityConfigurationService) {
  }

  private init(): Observable<void> {
    if (this.kcInstance) {
      return of(null);
    }
    const kcInstance: kc.KeycloakInstance = kc(this.configuration.keycloakConfiguration);
    return from(kcInstance.init({onLoad: 'check-sso', checkLoginIframe: false}))
      .pipe(tap(() => this.kcInstance = kcInstance), map(() => null));
  }

  public login(): Observable<void> {
    return this.init().pipe(
      flatMap(() => from(this.kcInstance.login())),
      tap(() => console.log(this.kcInstance.tokenParsed))
    );
  }

  public logout(): Observable<void> {
    return from(this.kcInstance.logout({redirectUri: document.baseURI}));
  }

  public get authenticated(): boolean {
    return this.kcInstance && !!this.kcInstance.authenticated;
  }

}
