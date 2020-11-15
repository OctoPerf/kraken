import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {GitConfigurationService} from 'projects/git/src/lib/git-configuration.service';
import {BehaviorSubject, Observable, of} from 'rxjs';
import {GitConfiguration} from 'projects/git/src/lib/entities/git-configuration';
import {finalize, tap} from 'rxjs/operators';
import {SSEService} from 'projects/sse/src/lib/sse.service';
import {EventBusService} from 'projects/event/src/lib/event-bus.service';
import {ReloadEventSourceEvent} from 'projects/sse/src/lib/events/reload-event-source-event';
import {GitStatusEvent} from 'projects/git/src/lib/events/git-status-event';

@Injectable({
  providedIn: 'root'
})
export class GitProjectService {

  public readonly configurationSubject: BehaviorSubject<GitConfiguration>;

  constructor(private http: HttpClient,
              private gitConfigurationService: GitConfigurationService,
              private eventBus: EventBusService) {
    this.configurationSubject = new BehaviorSubject(null);
  }

  public connect(repositoryUrl: string): Observable<GitConfiguration> {
    return this.http.post<GitConfiguration>(this.gitConfigurationService.projectApiUrl('/connect'), null, {
      params: {
        repositoryUrl
      }
    }).pipe(tap(configuration => {
      this.configurationSubject.next(configuration);
      // Force reconnection of the SSE service to get git events
      this.eventBus.publish(new ReloadEventSourceEvent());
    }));
  }

  public configuration(): Observable<GitConfiguration> {
    return of({repositoryUrl: ''});
    // return this.http.get<GitConfiguration>(this.gitConfigurationService.projectApiUrl('/configuration'))
    //   .pipe(tap(configuration => this.configurationSubject.next(configuration)));
  }

  public disconnect(): Observable<void> {
    return this.http.delete<void>(this.gitConfigurationService.projectApiUrl('/disconnect'))
      .pipe(finalize(() => {
        this.configurationSubject.next(null);
        // Force reconnection of the SSE service to stop getting git events
        this.eventBus.publish(new ReloadEventSourceEvent());
        // Clear status
        this.eventBus.publish(new GitStatusEvent({
          branch: {
            oid: '',
            head: '',
            behind: 0,
            ahead: 0,
            upstream: ''
          },
          changed: [],
          renamedCopied: [],
          unmerged: [],
          untracked: [],
          ignored: [],
        }));
      }));
  }

  public isConnected(): boolean {
    const config = this.configurationSubject.getValue();
    return !!config && !!config.repositoryUrl;
  }
}
