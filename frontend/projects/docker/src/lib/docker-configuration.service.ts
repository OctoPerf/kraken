import {Injectable} from '@angular/core';
import {ConfigurationService} from 'projects/commons/src/lib/config/configuration.service';

@Injectable({
  providedIn: 'root'
})
export class DockerConfigurationService {

  constructor(private configuration: ConfigurationService) {
  }

  dockerApiUrl(path: string): string {
    return this.configuration.url('dockerApiUrl', `/docker${path}`);
  }

}
