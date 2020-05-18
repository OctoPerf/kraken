import {TestBed} from '@angular/core/testing';

import {SecurityGuard} from './security.guard';
import SpyObj = jasmine.SpyObj;
import {SecurityService} from 'projects/security/src/lib/security.service';
import {securityServiceSpy} from 'projects/security/src/lib/security.service.spec';
import {Observable, of} from 'rxjs';
import {SecurityConfigurationService} from 'projects/security/src/lib/security-configuration.service';
import {securityConfigurationServiceSpy} from 'projects/security/src/lib/security-configuration.service.spec';

describe('SecurityGuard', () => {
  let guard: SecurityGuard;
  let service: SpyObj<SecurityService>;
  let config: SecurityConfigurationService;

  beforeEach(() => {
    service = securityServiceSpy();
    config = securityConfigurationServiceSpy();
    TestBed.configureTestingModule({
      providers: [
        {provide: SecurityService, useValue: service},
        {provide: SecurityConfigurationService, useValue: config}
      ]
    });
    guard = TestBed.inject(SecurityGuard);
  });

  it('should be created', () => {
    expect(guard).toBeTruthy();
  });

  it('should canActivate redirect login', () => {
    service.init.and.returnValue(of(false));
    service.login.and.returnValue(of(null));
    const response: Observable<boolean> = guard.canActivate(null, null) as any;
    response.subscribe(value => expect(value).toBeFalse());
  });

  it('should canActivate continue', () => {
    (service as any).roles = ['USER'];
    service.init.and.returnValue(of(true));
    const response: Observable<boolean> = guard.canActivate(null, null) as any;
    response.subscribe(value => expect(value).toBeTrue());
  });

  it('should canLoad redirect logout', () => {
    (service as any).roles = ['OTHER'];
    service.init.and.returnValue(of(true));
    service.login.and.returnValue(of(null));
    service.logout.and.returnValue(of(null));
    const response: Observable<boolean> = guard.canLoad(null, null) as any;
    response.subscribe(value => expect(value).toBeFalse());
  });
});

