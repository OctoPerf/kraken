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
  ]);
  spy.hostApiUrl.and.callFake((path = '') => `hostApiUrl/host${path}`);
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
});
