import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, Resolve, RouterStateSnapshot} from '@angular/router';
import {Observable} from 'rxjs';
import {GitConfiguration} from 'projects/git/src/lib/entities/git-configuration';
import {GitProjectService} from 'projects/git/src/lib/git-project/git-project.service';

@Injectable({
  providedIn: 'root'
})
export class GitConfigurationResolverService implements Resolve<GitConfiguration> {

  constructor(private gitProject: GitProjectService) {
  }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<GitConfiguration> {
    return this.gitProject.configuration();
  }
}
