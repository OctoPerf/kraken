import {Injectable} from '@angular/core';
import {BehaviorSubject, Observable, of} from 'rxjs';
import {Project} from 'projects/project/src/app/project/entities/project';
import {mergeMap, tap} from 'rxjs/operators';
import * as _ from 'lodash';
import {DefaultDialogService} from 'projects/dialog/src/lib/default-dialogs/default-dialog.service';
import {WindowService} from 'projects/tools/src/lib/window.service';
import {HttpClient} from '@angular/common/http';
import {ConfigurationService} from 'projects/commons/src/lib/config/configuration.service';

@Injectable({
  providedIn: 'root'
})
export class ProjectService {

  public projectsSubject: BehaviorSubject<Project[]>;
  private id = 100;

  constructor(private dialogs: DefaultDialogService,
              private window: WindowService,
              private configuration: ConfigurationService,
              private http: HttpClient) {
    this.projectsSubject = new BehaviorSubject([]);
  }

  public openProject(applicationId: string, projectId: string) {
    this.window.open(of(`${this.window.url()}/${applicationId}/${projectId}/`));
  }

  public updateProject(project: Project): Observable<Project> {
    return this.http.put<Project>(this.configuration.projectApiUrl(), project).pipe(tap(updated => {
      const projects = this.projectsSubject.getValue();
      const index = _.findIndex(projects, {id: project.id});
      projects[index] = updated;
      this.projectsSubject.next(projects);
    }));
  }

  public createProject(name: string, applicationId: string): Observable<Project> {
    return this.http.post<Project>(this.configuration.projectApiUrl(), null, {
      params: {
        applicationId,
        name,
      }
    }).pipe(tap(this.addProject.bind(this)));
  }

  public importFromGit(name: string, applicationId: string, repositoryUrl: string): Observable<Project> {
    return this.http.post<Project>(this.configuration.projectApiUrl('/import'), null, {
      params: {
        applicationId,
        name,
        repositoryUrl,
      }
    }).pipe(tap(this.addProject.bind(this)));
  }

  public deleteProject(project: Project, force = false): Observable<string> {
    const del = this.http.post(this.configuration.projectApiUrl('/delete'),
      project,
      {
        responseType: 'text',
      }).pipe(tap(str => {
      const projects = this.projectsSubject.getValue();
      _.remove(projects, current => current.id === project.id);
      this.projectsSubject.next(projects);
    }));
    return this.dialogs.delete('project', [project.name], force)
      .pipe(mergeMap(value => del));
  }

  public listProjects(): Observable<Project[]> {
    return this.http.get<Project[]>(this.configuration.projectApiUrl('/list')).pipe(tap(_projects => this.projectsSubject.next(_projects)));
  }

  private addProject(project: Project): void {
    const projects = this.projectsSubject.getValue();
    projects.push(project);
    this.projectsSubject.next(projects);
  }
}
