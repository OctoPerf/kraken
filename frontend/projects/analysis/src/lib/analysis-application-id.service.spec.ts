import {HttpHandler, HttpRequest} from '@angular/common/http';
import {ConfigurationService} from 'projects/commons/src/lib/config/configuration.service';
import {configurationServiceMock} from 'projects/commons/src/lib/config/configuration.service.spec';
import {AnalysisApplicationIdService} from 'projects/analysis/src/lib/analysis-application-id.service';
import {AnalysisConfigurationService} from 'projects/analysis/src/lib/analysis-configuration.service';
import {analysisConfigurationServiceSpy} from 'projects/analysis/src/lib/analysis-configuration.service.spec';

describe('AnalysisApplicationIdService', () => {

  let interceptor: AnalysisApplicationIdService;
  let next: HttpHandler;
  let configuration: ConfigurationService;
  let analysisConfiguration: AnalysisConfigurationService;

  beforeEach(() => {
    configuration = configurationServiceMock();
    analysisConfiguration = analysisConfigurationServiceSpy();
    interceptor = new AnalysisApplicationIdService(configuration, analysisConfiguration);
    next = jasmine.createSpyObj('next', ['handle']);
  });

  it('should be created', () => {
    expect(interceptor).toBeTruthy();
  });

  it('should intercept', () => {
    const req = new HttpRequest('GET', analysisConfiguration.analysisApiUrl('/path'));
    const intercepted = req.clone({
      headers: req.headers.set('ApplicationId', configuration.applicationId)
    });
    interceptor.intercept(req, next);
    expect(next.handle).toHaveBeenCalledWith(intercepted);
  });

  it('should not intercept', () => {
    const req = new HttpRequest('GET', 'apiUrl/path');
    interceptor.intercept(req, next);
    expect(next.handle).toHaveBeenCalledWith(req);
  });
});
