import {Injectable} from '@angular/core';
import {ConfigurationService} from 'projects/commons/src/lib/config/configuration.service';

@Injectable({
  providedIn: 'root'
})
export class DockerConfigurationService {

  constructor(private configuration: ConfigurationService) {
  }

  dockerContainerApiUrl(path: string = ''): string {
    return this.configuration.url('dockerApiUrl', `/container${path}`);
  }

  dockerImageApiUrl(path: string = ''): string {
    return this.configuration.url('dockerApiUrl', `/image${path}`);
  }

  dockerSystemApiUrl(path: string): string {
    return this.configuration.url('dockerApiUrl', `/system${path}`);
  }
}
