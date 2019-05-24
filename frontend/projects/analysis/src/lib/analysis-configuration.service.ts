import {Injectable} from '@angular/core';
import {ConfigurationService} from 'projects/commons/src/lib/config/configuration.service';
import {StorageNode} from 'projects/storage/src/lib/entities/storage-node';

@Injectable({
  providedIn: 'root'
})
export class AnalysisConfigurationService {

  constructor(private configuration: ConfigurationService) {
  }

  staticApiUrl(path: string): string {
    return this.configuration.url('staticApiUrl', path);
  }

  analysisApiUrl(path: string): string {
    return this.configuration.url('analysisApiUrl', path);
  }

  grafanaUrl(path: string): string {
    return this.configuration.url('grafanaUrl', path);
  }

  get analysisRootNode(): StorageNode {
    return this.configuration.value('analysisRootNode');
  }

}
