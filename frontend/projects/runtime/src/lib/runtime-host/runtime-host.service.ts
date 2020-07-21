import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {RuntimeConfigurationService} from 'projects/runtime/src/lib/runtime-configuration.service';
import {BehaviorSubject, Observable} from 'rxjs';
import {Host} from 'projects/runtime/src/lib/entities/host';
import {tap} from 'rxjs/operators';
import * as _ from 'lodash';

@Injectable()
export class RuntimeHostService {

  public hostsSubject: BehaviorSubject<Host[]>;
  public allSubject: BehaviorSubject<Host[]>;
  private readonly hostsMap = new Map<string, Host>();

  constructor(
    private http: HttpClient,
    private runtimeConfiguration: RuntimeConfigurationService,
  ) {
    this.hostsSubject = new BehaviorSubject([]);
    this.allSubject = new BehaviorSubject([]);
  }

  public hosts(): Observable<Host[]> {
    return this.http.get<Host[]>(this.runtimeConfiguration.hostApiUrl('/list')).pipe(tap(data => {
      this.hostsSubject.next(data);
      this.hostsMap.clear();
      data.forEach(host => this.hostsMap.set(host.id, host));
    }));
  }

  public all(): Observable<Host[]> {
    return this.http.get<Host[]>(this.runtimeConfiguration.hostApiUrl('/all')).pipe(tap(data => {
      this.allSubject.next(data);
    }));
  }

  public attach(host: Host, attached: Host): Observable<Host> {
    return this.http.post<Host>(this.runtimeConfiguration.hostApiUrl('/attach'), attached).pipe(tap(updated => {
      this.updateHost(host, updated);
    }));
  }

  public detach(host: Host): Observable<Host> {
    return this.http.post<Host>(this.runtimeConfiguration.hostApiUrl('/detach'), host).pipe(tap(updated => {
      this.updateHost(host, updated);
    }));
  }

  private updateHost(host: Host, updated: Host) {
    const all = this.allSubject.getValue();
    const index = _.indexOf(all, host);
    if (index !== -1) {
      all[index] = updated;
    }
    this.allSubject.next(all);
  }

  public host(id: string): Host | undefined {
    return this.hostsMap.get(id);
  }
}
