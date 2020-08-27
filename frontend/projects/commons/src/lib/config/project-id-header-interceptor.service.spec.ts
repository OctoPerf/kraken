import {HttpHandler, HttpRequest} from '@angular/common/http';
import {ConfigurationService} from 'projects/commons/src/lib/config/configuration.service';
import {configurationServiceMock} from 'projects/commons/src/lib/config/configuration.service.spec';
import {ProjectIdHeaderInterceptor} from 'projects/commons/src/lib/config/project-id-header-interceptor.service';

describe('ProjectIdHeaderInterceptor', () => {

  let interceptor: ProjectIdHeaderInterceptor;
  let next: HttpHandler;
  let configuration: ConfigurationService;

  beforeEach(() => {
    configuration = configurationServiceMock();
    interceptor = new ProjectIdHeaderInterceptor(configuration);
    next = jasmine.createSpyObj('next', ['handle']);
  });

  it('should be created', () => {
    expect(interceptor).toBeTruthy();
  });

  it('should intercept', () => {
    configuration.projectId = 'projectId';
    const req = new HttpRequest('GET', configuration.backendApiUrl + '/path');
    const intercepted = req.clone({
      headers: req.headers.set('ProjectId', configuration.projectId)
    });
    interceptor.intercept(req, next);
    expect(next.handle).toHaveBeenCalledWith(intercepted);
  });

  it('should not intercept (wrong url)', () => {
    const req = new HttpRequest('GET', 'apiUrl/path');
    interceptor.intercept(req, next);
    expect(next.handle).toHaveBeenCalledWith(req);
  });

  it('should intercept (no projectId)', () => {
    configuration.projectId = null;
    const req = new HttpRequest('GET', configuration.backendApiUrl + '/path');
    interceptor.intercept(req, next);
    expect(next.handle).toHaveBeenCalledWith(req);
  });
});
