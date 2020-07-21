import { Injectable } from '@angular/core';
import {ConfigurationService} from 'projects/commons/src/lib/config/configuration.service';

@Injectable({
  providedIn: 'root'
})
export class RuntimeConfigurationService {

  constructor(private configuration: ConfigurationService) {
  }

  hostApiUrl(path: string = ''): string {
    return this.configuration.url('backendApiUrl', `/host${path}`);
  }

  runtimeApiUrl(path: string = ''): string {
    return this.configuration.url('backendApiUrl', `/runtime${path}`);
  }

  taskApiUrl(path: string = ''): string {
    return this.configuration.url('backendApiUrl', `/task${path}`);
  }

  containerApiUrl(path: string = ''): string {
    return this.configuration.url('backendApiUrl', `/container${path}`);
  }
}
