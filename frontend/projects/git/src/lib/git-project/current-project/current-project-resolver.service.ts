import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, Resolve, RouterStateSnapshot} from '@angular/router';
import {Project} from 'projects/project/src/app/project/entities/project';
import {Observable} from 'rxjs';
import {CurrentProjectService} from 'projects/git/src/lib/git-project/current-project/current-project.service';

@Injectable({
  providedIn: 'root'
})
export class CurrentProjectResolverService implements Resolve<Project> {

  constructor(private currentProjectService: CurrentProjectService) {
  }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Project> {
    return this.currentProjectService.getProject();
  }
}
