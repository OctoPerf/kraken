import {Injectable} from '@angular/core';
import {StorageService} from 'projects/storage/src/lib/storage.service';
import {flatMap} from 'rxjs/operators';
import {Observable, of} from 'rxjs';
import {Result} from 'projects/analysis/src/lib/entities/result';
import {EventBusService} from 'projects/event/src/lib/event-bus.service';
import {WindowService} from 'projects/tools/src/lib/window.service';
import {AnalysisService} from 'projects/analysis/src/lib/analysis.service';
import {AnalysisConfigurationService} from 'projects/analysis/src/lib/analysis-configuration.service';
import {DialogService} from 'projects/dialog/src/lib/dialog.service';

@Injectable()
export class GatlingResultService {

  constructor(
    private storage: StorageService,
    private analysis: AnalysisService,
    private eventBus: EventBusService,
    private window: WindowService,
    private analysisConfiguration: AnalysisConfigurationService,
    private dialogs: DialogService,
  ) {
  }

  deleteResult(result: Result, force = false): Observable<string> {
    return this.dialogs.delete('test result', [result.description], force).pipe(flatMap(() => this.analysis.deleteTest(result.id)));
  }

  openGrafanaReport(result: Result) {
    this.window.open(of(this.analysisConfiguration.grafanaUrl(`/${result.id}`)));
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
