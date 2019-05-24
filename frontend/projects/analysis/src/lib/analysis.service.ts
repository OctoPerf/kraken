import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {AnalysisConfigurationService} from 'projects/analysis/src/lib/analysis-configuration.service';
import {ConfigurationService} from 'projects/commons/src/lib/config/configuration.service';

@Injectable({
  providedIn: 'root'
})
export class AnalysisService {

  constructor(
    private http: HttpClient,
    private analysisConfiguration: AnalysisConfigurationService,
  ) {
  }

  deleteTest(testId: string): Observable<string> {
    return this.http.delete(this.analysisConfiguration.analysisApiUrl('/delete'), {
      responseType: 'text',
      params: {
        testId
      }
    });
  }

  runTest(runDescription: string, environment: { [key in string]: string }): Observable<string> {
    return this._startTest(runDescription, environment, '/run');
  }

  debugTest(runDescription: string, environment: { [key in string]: string }): Observable<string> {
    return this._startTest(runDescription, environment, '/debug');
  }

  private _startTest(runDescription: string, environment: { [key in string]: string }, endpoint: string): Observable<string> {
    return this.http.post(this.analysisConfiguration.analysisApiUrl(endpoint), environment, {
      responseType: 'text',
      params: {
        runDescription,
      }
    });
  }

  record(environment: { [key in string]: string }): Observable<string> {
    return this.http.post(this.analysisConfiguration.analysisApiUrl('/record'), environment, {
      responseType: 'text'
    });
  }

}
