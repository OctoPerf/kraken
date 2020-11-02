import { Injectable } from '@angular/core';
import {ConfigurationService} from 'projects/commons/src/lib/config/configuration.service';

@Injectable({
  providedIn: 'root'
})
export class GitConfigurationService {

  constructor(private configuration: ConfigurationService) {
  }

  commandApiUrl(path: string = ''): string {
    return this.configuration.url('backendApiUrl', `/git/command${path}`);
  }

  projectApiUrl(path: string = ''): string {
    return this.configuration.url('backendApiUrl', `/git/project${path}`);
  }

  userApiUrl(path: string = ''): string {
    return this.configuration.url('backendApiUrl', `/git/user${path}`);
  }
}
