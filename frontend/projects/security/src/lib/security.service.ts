import {Injectable} from '@angular/core';
import {SecurityConfigurationService} from 'projects/security/src/lib/security-configuration.service';
import {from, Observable, of} from 'rxjs';
import * as kc from 'keycloak-js';
import {map, tap} from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class SecurityService {

  _kcInstance: kc.KeycloakInstance;

  constructor(private configuration: SecurityConfigurationService) {
  }

  public init(): Observable<boolean> {
    if (this._kcInstance) {
      return of(this.authenticated);
    }
    const kcInstance: kc.KeycloakInstance = kc(this.configuration.keycloakConfiguration);
    return from(kcInstance.init({onLoad: 'check-sso', checkLoginIframe: false}))
      .pipe(tap(() => this._kcInstance = kcInstance), map(() => this.authenticated));
  }

  public login(): Observable<void> {
    return from(this._kcInstance.login());
  }

  public logout(): Observable<void> {
    return from(this._kcInstance.logout({redirectUri: document.baseURI}));
  }

  public accountManagement(): Observable<void> {
    return from(this._kcInstance.accountManagement());
  }

  public get token(): Observable<string> {
    return from(this._kcInstance.updateToken(30)).pipe(
      // tap(() => console.log(this._kcInstance.token)),
      map(() => this._kcInstance.token)
    );
  }

  public get authenticated(): boolean {
    return this._kcInstance && !!this._kcInstance.authenticated;
  }

  public get username(): string {
    if (this._kcInstance && this._kcInstance.tokenParsed) {
      return (this._kcInstance.tokenParsed as any).preferred_username;
    }
  }

  public get roles(): string[] {
    if (this._kcInstance && this._kcInstance.tokenParsed) {
      return this._kcInstance.tokenParsed.realm_access.roles;
    }
  }

}
