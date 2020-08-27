import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, Resolve, RouterStateSnapshot} from '@angular/router';
import {Project} from 'projects/project/src/app/project/entities/project';
import {ProjectService} from 'projects/project/src/app/project/project.service';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ProjectListResolverService implements Resolve<Project[]> {

  constructor(private projectService: ProjectService) {
  }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Project[]> {
    return this.projectService.listProjects();
  }
}
