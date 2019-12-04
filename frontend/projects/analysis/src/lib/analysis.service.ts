import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {AnalysisConfigurationService} from 'projects/analysis/src/lib/analysis-configuration.service';

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
}
