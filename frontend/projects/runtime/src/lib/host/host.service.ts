import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {RuntimeConfigurationService} from 'projects/runtime/src/lib/runtime-configuration.service';
import {BehaviorSubject, Observable} from 'rxjs';
import {Host} from 'projects/runtime/src/lib/entities/host';
import {tap} from 'rxjs/operators';

@Injectable()
export class HostService {

  public hostsSubject: BehaviorSubject<Host[]>;

  constructor(
    private http: HttpClient,
    private runtimeConfiguration: RuntimeConfigurationService,
  ) {
    this.hostsSubject = new BehaviorSubject([]);
  }

  hosts(): Observable<Host[]> {
    return this.http.get<Host[]>(this.runtimeConfiguration.hostApiUrl('/list')).pipe(tap(data => this.hostsSubject.next(data)));
  }
}
