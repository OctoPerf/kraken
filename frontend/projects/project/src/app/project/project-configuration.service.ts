import {Injectable} from '@angular/core';
import {ConfigurationService} from 'projects/commons/src/lib/config/configuration.service';

@Injectable({
  providedIn: 'root'
})
export class ProjectConfigurationService {

  constructor(private configuration: ConfigurationService) {
  }

  availableApplications(): string[] {
    return this.configuration.value('availableApplications');
  }
}
