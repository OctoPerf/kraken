import {Injectable} from '@angular/core';
import {DialogService} from 'projects/dialog/src/lib/dialog.service';
import {HttpClient} from '@angular/common/http';
import {BehaviorSubject, Observable} from 'rxjs';
import {DockerImage} from 'projects/docker/src/lib/entities/docker-image';
import {DockerContainer} from 'projects/docker/src/lib/entities/docker-container';
import * as _ from 'lodash';
import {map, tap} from 'rxjs/operators';
import {DockerConfigurationService} from 'projects/docker/src/lib/docker-configuration.service';

@Injectable()
export class DockerService {

  public containersSubject: BehaviorSubject<DockerContainer[]>;
  public imagesSubject: BehaviorSubject<DockerImage[]>;

  constructor(private configuration: DockerConfigurationService,
              private dialogs: DialogService,
              private http: HttpClient) {
    this.containersSubject = new BehaviorSubject([]);
    this.imagesSubject = new BehaviorSubject([]);
  }

  images(): Observable<DockerImage[]> {
    return this.http.get<DockerImage[]>(this.configuration.dockerApiUrl('/images')).pipe(tap(data => this.imagesSubject.next(data)));
  }

  rmi(image: DockerImage): Observable<boolean> {
    return this.http.delete(this.configuration.dockerApiUrl(`/rmi`), {
      responseType: 'text',
      params: {
        imageId: image.id
      }
    }).pipe(map(str => str === 'true'), tap((bool) => {
      if (bool) {
        const filtered = _.without(this.imagesSubject.value, image);
        this.imagesSubject.next(filtered);
      }
    }));
  }

  pull(image: string): Observable<string> {
    return this.http.get(this.configuration.dockerApiUrl('/pull'), {
      responseType: 'text',
      params: {
        image,
      }
    });
  }

  ps(): Observable<DockerContainer[]> {
    return this.http.get<DockerContainer[]>(this.configuration.dockerApiUrl('/ps')).pipe(tap(data => this.containersSubject.next(data)));
  }

  rm(container: DockerContainer): Observable<boolean> {
    return this.http.delete(this.configuration.dockerApiUrl(`/rm`), {
      responseType: 'text',
      params: {
        containerId: container.id
      }
    }).pipe(map(str => str === 'true'), tap((bool) => {
      if (bool) {
        const filtered = _.without(this.containersSubject.value, container);
        this.containersSubject.next(filtered);
      }
    }));
  }

  tail(container: DockerContainer): Observable<string> {
    return this.http.get(this.configuration.dockerApiUrl('/tail'), {
      responseType: 'text',
      params: {
        containerId: container.id,
        lines: '3',
      }
    });
  }

  logs(container: DockerContainer): Observable<string> {
    return this.http.get(this.configuration.dockerApiUrl('/logs'), {
      responseType: 'text',
      params: {
        containerId: container.name,
      }
    });
  }

  start(container: DockerContainer): Observable<string> {
    return this.http.get(this.configuration.dockerApiUrl('/start'), {
      responseType: 'text',
      params: {
        containerId: container.id,
      }
    }).pipe(tap(() => this.ps().subscribe()));
  }

  stop(container: DockerContainer): Observable<string> {
    return this.http.get(this.configuration.dockerApiUrl('/stop'), {
      responseType: 'text',
      params: {
        containerId: container.id,
      }
    }).pipe(tap(() => this.ps().subscribe()));
  }

  run(name: string, config: string): Observable<string> {
    return this.http.post(this.configuration.dockerApiUrl('/run'), config, {
      responseType: 'text',
      params: {
        name,
      }
    }).pipe(tap((containerId: string) => this.logs({
      id: containerId,
      name: name,
      image: '',
      status: '',
      full: {}
    }).subscribe()));
  }

  prune(all: boolean, volumes: boolean): Observable<string> {
    return this.http.get(this.configuration.dockerApiUrl('/prune'), {
      responseType: 'text',
      params: {
        all: all.toString(),
        volumes: volumes.toString(),
      }
    });
  }
}
