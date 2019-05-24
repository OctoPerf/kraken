import {Injectable} from '@angular/core';
import {StorageService} from 'projects/storage/src/lib/storage.service';
import {catchError, map} from 'rxjs/operators';
import {StorageNode} from 'projects/storage/src/lib/entities/storage-node';
import {Observable, of} from 'rxjs';
import {Result} from 'projects/analysis/src/lib/entities/result';
import {EventBusService} from 'projects/event/src/lib/event-bus.service';
import {WindowService} from 'projects/tools/src/lib/window.service';
import {NotificationEvent} from 'projects/notification/src/lib/notification-event';
import {BaseNotification} from 'projects/notification/src/lib/base-notification';
import {GatlingConfigurationService} from 'projects/gatling/src/app/gatling-configuration.service';
import {AnalysisService} from 'projects/analysis/src/lib/analysis.service';
import {AnalysisConfigurationService} from 'projects/analysis/src/lib/analysis-configuration.service';
import {NotificationLevel} from 'projects/notification/src/lib/notification-level';

@Injectable()
export class GatlingResultService {

  private rootNode: StorageNode;

  constructor(
    private storage: StorageService,
    private analysis: AnalysisService,
    private eventBus: EventBusService,
    private window: WindowService,
    private analysisConfiguration: AnalysisConfigurationService,
  ) {
    this.rootNode = this.analysisConfiguration.analysisRootNode;
  }

  deleteResult(result: Result): Observable<string> {
    return this.analysis.deleteTest(result.id);
  }

  openGrafanaReport(result: Result) {
    this.window.open(of(this.analysisConfiguration.grafanaUrl(`/${result.id}`)));
  }

  openGatlingReport(result: Result) {
    const path = this.rootNode.path + '/' + result.id;
    const url = this.storage.find(path, 'index\.html')
      .pipe(map((nodes: StorageNode[]) => {
        if (nodes.length) {
          return this.analysisConfiguration.staticApiUrl('/' + nodes[0].path);
        } else {
          throw Error('No report node found');
        }
      }), catchError(err => {
        this.eventBus.publish(new NotificationEvent(
          new BaseNotification('Cannot open Gatling report. \'index.html\' could not be found.', NotificationLevel.ERROR)
        ));
        throw err;
      }));
    this.window.open(url);
  }

  canOpenGrafanaReport(result: Result) {
    return result && (result.type === 'RUN' && result.status !== 'FAILED');
  }

  canOpenGatlingReport(result: Result) {
    return result && (result.type !== 'HAR' && result.status === 'COMPLETED' || result.status === 'CANCELED');
  }

  canDeleteResult(result: Result) {
    return result && (result.status === 'COMPLETED' || result.status === 'CANCELED' || result.status === 'FAILED');
  }
}
