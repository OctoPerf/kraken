import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {GitConfigurationService} from 'projects/git/src/lib/git-configuration.service';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class GitUserService {

  constructor(private http: HttpClient,
              private gitConfigurationService: GitConfigurationService) {
  }

  public publicKey(): Observable<string> {
    return this.http.get(this.gitConfigurationService.userApiUrl('/publicKey'), {
      responseType: 'text',
    });
  }

}
