import {Injectable} from '@angular/core';
import {BehaviorSubject, Observable} from 'rxjs';
import {Project} from 'projects/project/src/app/project/entities/project';
import {ConfigurationService} from 'projects/commons/src/lib/config/configuration.service';
import {HttpClient} from '@angular/common/http';
import {tap} from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class CurrentProjectService {

  public currentProject: BehaviorSubject<Project>;

  constructor(private configuration: ConfigurationService,
              private http: HttpClient) {
    this.currentProject = new BehaviorSubject<Project>(null);
  }

  public getProject(): Observable<Project> {
    return this.http.get<Project>(this.configuration.projectApiUrl()).pipe(tap(project => this.currentProject.next(project)));
  }
}
