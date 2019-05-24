import {Injectable} from '@angular/core';
import {ConfigurationService} from 'projects/commons/src/lib/config/configuration.service';

@Injectable({
  providedIn: 'root'
})
export class CommandConfigurationService {

  constructor(private configuration: ConfigurationService) {
  }

  commandApiUrl(path: string): string {
    return this.configuration.url('executorApiUrl', `/command${path}`);
  }

}
