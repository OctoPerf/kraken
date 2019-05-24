import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {EventBusService} from 'projects/event/src/lib/event-bus.service';
import {DockerConfigurationService} from 'projects/docker/src/lib/docker-configuration.service';

@Injectable()
export class DockerComposeService {

  constructor(private configuration: DockerConfigurationService,
              private eventBus: EventBusService,
              private http: HttpClient) {
  }

  command(command: 'up' | 'down' | 'ps' | 'logs', path: string): Observable<string> {
    return this.http.get(this.configuration.dockerComposeApiUrl(`/${command}`), {
      responseType: 'text',
      params: {
        path,
      }
    });
  }

}
