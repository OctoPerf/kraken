import {Injectable} from '@angular/core';
import {ConfigurationService} from 'projects/commons/src/lib/config/configuration.service';
import {StorageNode} from 'projects/storage/src/lib/entities/storage-node';

@Injectable({
  providedIn: 'root'
})
export class StorageConfigurationService {

  constructor(private configuration: ConfigurationService) {
  }

  storageApiUrl(path: string = ''): string {
    return this.configuration.url('storageApiUrl', `/files${path}`);
  }

  get readmeNode(): StorageNode {
    return this.configuration.value('readmeNode');
  }
}
