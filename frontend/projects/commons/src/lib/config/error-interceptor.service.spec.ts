import {TestBed} from '@angular/core/testing';

import {ErrorInterceptor} from './error-interceptor.service';
import {HttpErrorResponse, HttpHandler, HttpRequest} from '@angular/common/http';
import Spy = jasmine.Spy;
import {throwError} from 'rxjs';
import {RestServerError} from './rest-server-error';
import {eventBusSpy} from 'projects/event/src/lib/event-bus.service.spec';
import {EventBusService} from 'projects/event/src/lib/event-bus.service';
import {ConfigurationService} from 'projects/commons/src/lib/config/configuration.service';
import {configurationServiceMock} from 'projects/commons/src/lib/config/configuration.service.spec';

describe('ErrorInterceptor', () => {

  let interceptor: ErrorInterceptor;
  let next: HttpHandler;
  let eventBus: EventBusService;
  let configuration: ConfigurationService;

  beforeEach(() => {
    eventBus = eventBusSpy();
    configuration = configurationServiceMock();

    TestBed.configureTestingModule({
        providers: [
          {provide: ErrorInterceptor, useValue: new ErrorInterceptor(eventBus, configuration)}
        ]
      }
    );
    interceptor = TestBed.get(ErrorInterceptor);
    next = jasmine.createSpyObj('next', ['handle']);
  });

  it('should be created', () => {
    expect(interceptor).toBeTruthy();
  });

  it('should intercept', () => {
    (next.handle as Spy).and.returnValue(throwError(new HttpErrorResponse({
      status: 404,
      error: JSON.stringify({type: 'ItemNotFound', message: 'message'})
    })));
    const req = new HttpRequest('GET', 'apiUrl/path');
    interceptor.intercept(req, next).subscribe(() => fail('should return error'),
      (error) => expect(error).toEqual(new RestServerError('Error 404', 'message')));
    expect(next.handle).toHaveBeenCalledWith(req);
    expect(eventBus.publish).toHaveBeenCalled();
  });

  it('should not intercept', () => {
    const httpError = new HttpErrorResponse({
      status: 404,
      error: JSON.stringify({type: 'ItemNotFound', message: 'message'})
    });
    (next.handle as Spy).and.returnValue(throwError(httpError));
    configuration._errorsMatcherRegexp = new RegExp('FAIL');
    const req = new HttpRequest('GET', 'apiUrl/path');
    interceptor.intercept(req, next).subscribe(() => fail('should return error'),
      (error) => expect(error).toEqual(httpError));
    expect(next.handle).toHaveBeenCalledWith(req);
    expect(eventBus.publish).not.toHaveBeenCalled();
  });
});
