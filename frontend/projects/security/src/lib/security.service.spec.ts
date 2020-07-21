import {TestBed} from '@angular/core/testing';

import {SecurityService} from './security.service';
import {SecurityConfigurationService} from 'projects/security/src/lib/security-configuration.service';
import {securityConfigurationServiceSpy} from 'projects/security/src/lib/security-configuration.service.spec';

export const securityServiceSpy = () => {
  const spy = jasmine.createSpyObj('SecurityService', [
    'init',
    'login',
    'logout',
  ]);
  return spy;
};

describe('SecurityService', () => {
  let service: SecurityService;
  let configuration: SecurityConfigurationService;

  beforeEach(() => {
    configuration = securityConfigurationServiceSpy();
    TestBed.configureTestingModule({
      providers: [
        {provide: SecurityConfigurationService, useValue: configuration},
      ]
    });
    service = TestBed.inject(SecurityService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should login', () => {
    const kcInstance = jasmine.createSpyObj('kc', ['login']);
    kcInstance.tokenParser = 'tokenParsed';
    kcInstance.login.and.returnValue(new Promise(resolve => {}));
    service._kcInstance = kcInstance;
    expect(service.login().subscribe()).toBeTruthy();
  });

  it('should logout', () => {
    const kcInstance = jasmine.createSpyObj('kc', ['logout']);
    kcInstance.logout.and.returnValue(new Promise(resolve => {}));
    service._kcInstance = kcInstance;
    expect(service.logout().subscribe()).toBeTruthy();
  });

  it('should accountManagement', () => {
    const kcInstance = jasmine.createSpyObj('kc', ['accountManagement']);
    service._kcInstance = kcInstance;
    kcInstance.accountManagement.and.returnValue(new Promise(resolve => {}));
    expect(service.accountManagement().subscribe()).toBeTruthy();
  });

  it('should token', () => {
    const kcInstance = jasmine.createSpyObj('kc', ['updateToken']);
    kcInstance.token = 'token';
    kcInstance.updateToken.and.returnValue(new Promise(resolve => {}));
    service._kcInstance = kcInstance;
    service.token.subscribe(value => expect(value).toEqual('token'));
  });

  it('should authenticated', () => {
    expect(service.authenticated).toBeFalsy();
    const kcInstance: any = {
      authenticated: false
    };
    service._kcInstance = kcInstance;
    expect(service.authenticated).toBeFalse();
    kcInstance.authenticated = true;
    expect(service.authenticated).toBeTrue();
  });

  it('should username', () => {
    service._kcInstance = {
      tokenParsed: {
        preferred_username: 'username'
      }
    } as any;
    expect(service.username).toBe('username');
  });

  it('should roles', () => {
    service._kcInstance = {
      tokenParsed: {
        realm_access: {
          roles: ['USER']
        }
      }
    } as any;
    expect(service.roles).toEqual(['USER']);
  });
});
