import {TestBed} from '@angular/core/testing';

import {RuntimeConfigurationService} from './runtime-configuration.service';
import {AnalysisConfigurationService} from 'projects/analysis/src/lib/analysis-configuration.service';
import {ConfigurationService} from 'projects/commons/src/lib/config/configuration.service';
import {configurationServiceSpy} from 'projects/commons/src/lib/config/configuration.service.spec';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import SpyObj = jasmine.SpyObj;

export const runtimeConfigurationServiceSpy = () => {
  const spy = jasmine.createSpyObj('RuntimeConfigurationService', [
    'hostApiUrl',
    'runtimeApiUrl',
  ]);
  spy.hostApiUrl.and.callFake((path = '') => `hostApiUrl/host${path}`);
  spy.runtimeApiUrl.and.callFake((path = '') => `runtimeApiUrl/runtime${path}`);
  return spy;
};

describe('RuntimeConfigurationService', () => {
  let service: RuntimeConfigurationService;
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
    service = TestBed.get(RuntimeConfigurationService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should return hostApiUrl', () => {
    configuration.url.and.returnValue('url');
    expect(service.hostApiUrl('/path')).toBe('url');
    expect(configuration.url).toHaveBeenCalledWith('runtimeApiUrl', '/host/path');
  });

  it('should return hostApiUrl no param', () => {
    configuration.url.and.returnValue('url');
    expect(service.hostApiUrl()).toBe('url');
    expect(configuration.url).toHaveBeenCalledWith('runtimeApiUrl', '/host');
  });

  it('should return runtimeApiUrl', () => {
    configuration.url.and.returnValue('url');
    expect(service.runtimeApiUrl('/path')).toBe('url');
    expect(configuration.url).toHaveBeenCalledWith('runtimeApiUrl', '/runtime/path');
  });

  it('should return runtimeApiUrl no param', () => {
    configuration.url.and.returnValue('url');
    expect(service.runtimeApiUrl()).toBe('url');
    expect(configuration.url).toHaveBeenCalledWith('runtimeApiUrl', '/runtime');
  });
});
