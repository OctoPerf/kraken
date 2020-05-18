import {Injectable} from '@angular/core';
import {StorageService} from 'projects/storage/src/lib/storage.service';
import {flatMap, map, tap} from 'rxjs/operators';
import {Observable, of} from 'rxjs';
import {Result} from 'projects/analysis/src/lib/entities/result';
import {EventBusService} from 'projects/event/src/lib/event-bus.service';
import {WindowService} from 'projects/tools/src/lib/window.service';
import {AnalysisService} from 'projects/analysis/src/lib/analysis.service';
import {AnalysisConfigurationService} from 'projects/analysis/src/lib/analysis-configuration.service';
import {DialogService} from 'projects/dialog/src/lib/dialog.service';
import {HttpClient} from '@angular/common/http';
import {CookieService} from 'ngx-cookie-service';

@Injectable()
export class GatlingResultService {

  constructor(
    private storage: StorageService,
    private analysis: AnalysisService,
    private eventBus: EventBusService,
    private window: WindowService,
    private analysisConfiguration: AnalysisConfigurationService,
    private dialogs: DialogService,
    private http: HttpClient,
    private cookieService: CookieService,
  ) {
  }

  deleteResult(result: Result, force = false): Observable<string> {
    return this.dialogs.delete('test result', [result.description], force).pipe(flatMap(() => this.analysis.deleteTest(result.id)));
  }

  openGrafanaReport(result: Result) {
    const grafanaLogin = this.http.get(this.analysisConfiguration.analysisApiUrl('/grafana/login'), {
      params: {
        resultId: result.id
      }
    }).pipe(tap((value: any) => {
      console.log(value);
      this.cookieService.delete('grafana_session');
      this.cookieService.set('grafana_session', value.session, null, '/grafana/', null, false, 'Lax');
    }), map((value: any) => value.url));
    this.window.open(grafanaLogin);
  }

  canOpenGrafanaReport(result: Result) {
    return result && result.type === 'RUN';
  }

  canOpenGatlingReport(result: Result) {
    return result && result.type !== 'HAR' && (result.status === 'COMPLETED' || result.status === 'CANCELED');
  }

  canDeleteResult(result: Result) {
    return result && (result.status === 'COMPLETED' || result.status === 'CANCELED' || result.status === 'FAILED');
  }
}
