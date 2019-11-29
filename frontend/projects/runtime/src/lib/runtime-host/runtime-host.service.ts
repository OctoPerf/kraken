import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {RuntimeConfigurationService} from 'projects/runtime/src/lib/runtime-configuration.service';
import {BehaviorSubject, Observable} from 'rxjs';
import {Host} from 'projects/runtime/src/lib/entities/host';
import {tap} from 'rxjs/operators';

@Injectable()
export class RuntimeHostService {

  public hostsSubject: BehaviorSubject<Host[]>;
  private readonly hostsMap = new Map<string, Host>();

  constructor(
    private http: HttpClient,
    private runtimeConfiguration: RuntimeConfigurationService,
  ) {
    this.hostsSubject = new BehaviorSubject([]);
  }

  public hosts(): Observable<Host[]> {
    return this.http.get<Host[]>(this.runtimeConfiguration.hostApiUrl('/list')).pipe(tap(data => {
      this.hostsSubject.next(data);
      this.hostsMap.clear();
      data.forEach(host => this.hostsMap.set(host.id, host));
    }));
  }

  public host(id: string): Host | undefined {
    return this.hostsMap.get(id);
  }
}
