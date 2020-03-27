import {HttpHandler, HttpRequest} from '@angular/common/http';
import {ApplicationIdHeaderInterceptor} from 'projects/commons/src/lib/config/application-id-header-interceptor.service';
import {ConfigurationService} from 'projects/commons/src/lib/config/configuration.service';
import {configurationServiceMock} from 'projects/commons/src/lib/config/configuration.service.spec';
import {SecurityInterceptor} from 'projects/security/src/lib/security-interceptor.service';

describe('SecurityInterceptor', () => {

  let interceptor: SecurityInterceptor;
  let next: HttpHandler;
  let configuration: ConfigurationService;

  beforeEach(() => {
    configuration = configurationServiceMock();
    // interceptor = new SecurityInterceptor(configuration, () => [configuration.runtimeApiUrl]);
    interceptor = null;
    next = jasmine.createSpyObj('next', ['handle']);
  });

  it('should be created', () => {
    expect(interceptor).toBeTruthy();
  });

  it('should intercept', () => {
    // const req = new HttpRequest('GET', configuration.runtimeApiUrl + '/path');
    // const intercepted = req.clone({
    //   headers: req.headers.set('ApplicationId', configuration.applicationId)
    // });
    // interceptor.intercept(req, next);
    // expect(next.handle).toHaveBeenCalledWith(intercepted);
  });

  it('should not intercept', () => {
    // const req = new HttpRequest('GET', 'apiUrl/path');
    // interceptor.intercept(req, next);
    // expect(next.handle).toHaveBeenCalledWith(req);
  });
});
