import {Inject, Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Configuration} from './configuration';
import {BehaviorSubject} from 'rxjs';
import {ENVIRONMENT} from 'projects/commons/src/lib/config/configuration-environment';

export function loadConfiguration(configurationService: ConfigurationService) {
  return () => configurationService.load();
}

@Injectable({
  providedIn: 'root'
})
export class ConfigurationService {

  _config = new BehaviorSubject<Configuration>({
    version: 'version',
    applicationId: 'application',
    commandApiUrl: 'commandApiUrl',
    dockerApiUrl: 'dockerApiUrl',
    storageApiUrl: 'storageApiUrl',
    docUrl: 'docUrl'
  });

  constructor(private http: HttpClient,
              @Inject(ENVIRONMENT) private env) {
  }

  load(): Promise<Configuration> {
    console.log(`Loading configuration from environment ${this.env.configUrl}`);
    const $promise = this.http.get<Configuration>(this.env.configUrl).toPromise();
    $promise.then((config: Configuration) => {
      console.log(config);
      this._config.next(config);
    });
    return $promise;
  }

  get applicationId(): string {
    return this._config.value.applicationId;
  }

  get commandApiUrl(): string {
    return this.value('commandApiUrl');
  }

  get dockerApiUrl(): string {
    return this.value('dockerApiUrl');
  }

  get runtimeApiUrl(): string {
    return this.value('runtimeApiUrl');
  }

  get storageApiUrl(): string {
    return this.value('storageApiUrl');
  }

  docUrl(path: string): string {
    return this.url('docUrl', path);
  }

  version(key: string) {
    return `${this._config.value.version}_${key}`;
  }

  url(key: string, path: string): string {
    return this._config.value[key] + path;
  }

  value(key: string): any {
    return this._config.value[key];
  }
}
