import {HttpHandler, HttpRequest} from '@angular/common/http';
import {ApplicationIdHeaderInterceptor} from 'projects/commons/src/lib/config/application-id-header-interceptor.service';
import {ConfigurationService} from 'projects/commons/src/lib/config/configuration.service';
import {configurationServiceMock} from 'projects/commons/src/lib/config/configuration.service.spec';

describe('ApplicationIdHeaderInterceptor', () => {

  let interceptor: ApplicationIdHeaderInterceptor;
  let next: HttpHandler;
  let configuration: ConfigurationService;

  beforeEach(() => {
    configuration = configurationServiceMock();
    interceptor = new ApplicationIdHeaderInterceptor(configuration, () => configuration.executorApiUrl(''));
    next = jasmine.createSpyObj('next', ['handle']);
  });

  it('should be created', () => {
    expect(interceptor).toBeTruthy();
  });

  it('should intercept', () => {
    const req = new HttpRequest('GET', configuration.executorApiUrl('/path'));
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
