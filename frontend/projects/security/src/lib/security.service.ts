import {Injectable} from '@angular/core';
import {SecurityConfigurationService} from 'projects/security/src/lib/security-configuration.service';
import {from, Observable, of} from 'rxjs';
import * as kc from 'keycloak-js';
import {map, tap} from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class SecurityService {

  private kcInstance: kc.KeycloakInstance;

  constructor(private configuration: SecurityConfigurationService) {
  }

  public init(): Observable<boolean> {
    if (this.kcInstance) {
      return of(this.authenticated);
    }
    const kcInstance: kc.KeycloakInstance = kc(this.configuration.keycloakConfiguration);
    return from(kcInstance.init({onLoad: 'check-sso', checkLoginIframe: false}))
      .pipe(tap(() => this.kcInstance = kcInstance), map(() => this.authenticated));
  }

  public login(): Observable<void> {
    return from(this.kcInstance.login()).pipe(
      tap(() => console.log(this.kcInstance.tokenParsed))
    );
  }

  public logout(): Observable<void> {
    return from(this.kcInstance.logout({redirectUri: document.baseURI}));
  }

  public accountManagement(): Observable<void> {
    return from(this.kcInstance.accountManagement());
  }

  public get token(): Observable<string> {
    return from(this.kcInstance.updateToken(30)).pipe(
      map(() => this.kcInstance.token)
    );
  }

  public get authenticated(): boolean {
    return this.kcInstance && !!this.kcInstance.authenticated;
  }

  public get username(): string {
    return (this.kcInstance.tokenParsed as any).preferred_username;
  }
}
