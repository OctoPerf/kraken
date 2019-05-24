import {Injectable} from '@angular/core';
import {ConfigurationService} from 'projects/commons/src/lib/config/configuration.service';
import {StorageNode} from 'projects/storage/src/lib/entities/storage-node';

@Injectable({
  providedIn: 'root'
})
export class GatlingConfigurationService {

  constructor(private configuration: ConfigurationService) {
  }

  get simulationsRootNode(): StorageNode {
    return this.configuration.value('gatlingSimulationsRootNode');
  }

  get resourcesRootNode(): StorageNode {
    return this.configuration.value('gatlingResourcesRootNode');
  }

}
