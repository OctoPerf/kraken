import {Inject, Injectable, InjectionToken} from '@angular/core';
import {ConfigurationService} from 'projects/commons/src/lib/config/configuration.service';

export const STORAGE_ENDPOINT_ROOT = new InjectionToken<string>('StorageEndpointRoot');

@Injectable()
export class StorageEndpointService {

  constructor(@Inject(STORAGE_ENDPOINT_ROOT) private _root: string,
              private config: ConfigurationService) {
  }

  path(suffix: string): string {
    return this.config.storageApiUrl(`${this._root}${suffix}`);
  }

  get root(): string {
    return this._root;
  }
}
