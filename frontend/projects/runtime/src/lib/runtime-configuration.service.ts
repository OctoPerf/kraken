import { Injectable } from '@angular/core';
import {ConfigurationService} from 'projects/commons/src/lib/config/configuration.service';

@Injectable({
  providedIn: 'root'
})
export class RuntimeConfigurationService {

  constructor(private configuration: ConfigurationService) {
  }

  hostApiUrl(path: string = ''): string {
    return this.configuration.url('runtimeApiUrl', `/host${path}`);
  }

  runtimeApiUrl(path: string = ''): string {
    return this.configuration.url('runtimeApiUrl', `/runtime${path}`);
  }
}
