import {Injectable} from '@angular/core';
import {ConfigurationService} from 'projects/commons/src/lib/config/configuration.service';
import {KeycloakConfig} from 'keycloak-js';

@Injectable({
  providedIn: 'root'
})
export class SecurityConfigurationService {

  constructor(private configuration: ConfigurationService) {
  }

  get keycloakConfiguration(): KeycloakConfig {
    return this.configuration.value('keycloakConfiguration');
  }

  get expectedRole(): string[] {
    return this.configuration.value('expectedRoles');
  }
}
