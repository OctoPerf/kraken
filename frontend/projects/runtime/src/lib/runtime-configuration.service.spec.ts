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
    'taskApiUrl',
    'containerApiUrl',
  ]);
  spy.hostApiUrl.and.callFake((path = '') => `hostApiUrl/host${path}`);
  spy.runtimeApiUrl.and.callFake((path = '') => `runtimeApiUrl/runtime${path}`);
  spy.taskApiUrl.and.callFake((path = '') => `taskApiUrl/task${path}`);
  spy.containerApiUrl.and.callFake((path = '') => `containerApiUrl/container${path}`);
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

  it('should return taskApiUrl', () => {
    configuration.url.and.returnValue('url');
    expect(service.taskApiUrl('/path')).toBe('url');
    expect(configuration.url).toHaveBeenCalledWith('runtimeApiUrl', '/task/path');
  });

  it('should return taskApiUrl no param', () => {
    configuration.url.and.returnValue('url');
    expect(service.taskApiUrl()).toBe('url');
    expect(configuration.url).toHaveBeenCalledWith('runtimeApiUrl', '/task');
  });

  it('should return containerApiUrl', () => {
    configuration.url.and.returnValue('url');
    expect(service.containerApiUrl('/path')).toBe('url');
    expect(configuration.url).toHaveBeenCalledWith('runtimeApiUrl', '/container/path');
  });

  it('should return containerApiUrl no param', () => {
    configuration.url.and.returnValue('url');
    expect(service.containerApiUrl()).toBe('url');
    expect(configuration.url).toHaveBeenCalledWith('runtimeApiUrl', '/container');
  });
});
