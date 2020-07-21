import {TestBed} from '@angular/core/testing';

import {SecurityConfigurationService} from './security-configuration.service';
import {KeycloakConfig} from 'keycloak-js';
import {ConfigurationService} from 'projects/commons/src/lib/config/configuration.service';
import {configurationServiceSpy} from 'projects/commons/src/lib/config/configuration.service.spec';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import SpyObj = jasmine.SpyObj;

export const testKeycloakConfiguration: () => KeycloakConfig = () => {
  return {
    url: 'http://localhost:9080/auth',
    realm: 'kraken',
    clientId: 'kraken-web'
  };
};

export const securityConfigurationServiceSpy = () => {
  return {
    keycloakConfiguration: testKeycloakConfiguration(),
    expectedRole: ['USER', 'ADMIN'],
  } as SecurityConfigurationService;
};

describe('SecurityConfigurationService', () => {
  let service: SecurityConfigurationService;
  let configuration: SpyObj<ConfigurationService>;

  beforeEach(() => {
    configuration = configurationServiceSpy();
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [
        ConfigurationService,
        {
          provide: ConfigurationService,
          useValue: configuration,
        },
      ]
    });
    service = TestBed.inject(SecurityConfigurationService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should return keycloakConfiguration', () => {
    configuration.value.and.returnValue(testKeycloakConfiguration());
    expect(service.keycloakConfiguration).toEqual(testKeycloakConfiguration());
  });

  it('should return expectedRole', () => {
    configuration.value.and.returnValue(['USER']);
    expect(service.expectedRole).toEqual(['USER']);
  });

});
