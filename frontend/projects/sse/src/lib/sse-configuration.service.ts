import { Injectable } from '@angular/core';
import {ConfigurationService} from 'projects/commons/src/lib/config/configuration.service';

@Injectable({
  providedIn: 'root'
})
export class SSEConfigurationService {

  constructor(private configuration: ConfigurationService) {
  }

  sseApiUrl(path: string = ''): string {
    return this.configuration.url('sseApiUrl', `/sse${path}`);
  }

}
