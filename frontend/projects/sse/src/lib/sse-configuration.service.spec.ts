import {TestBed} from '@angular/core/testing';

import {SSEConfigurationService} from 'projects/sse/src/lib/sse-configuration.service';
import {ConfigurationService} from 'projects/commons/src/lib/config/configuration.service';
import {configurationServiceSpy} from 'projects/commons/src/lib/config/configuration.service.spec';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import SpyObj = jasmine.SpyObj;

export const sseConfigurationServiceSpy = () => {
  const spy = jasmine.createSpyObj('SSEConfigurationService', [
    'sseApiUrl',
    'sseChannels',
  ]);
  spy.sseApiUrl.and.callFake((path = '') => `backendApiUrl/sse${path}`);
  spy.sseChannels.and.returnValue(['STORAGE', 'RUNTIME']);
  return spy;
};

describe('SSEConfigurationService', () => {
  let service: SSEConfigurationService;
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
    service = TestBed.inject(SSEConfigurationService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should return sseApiUrl', () => {
    configuration.url.and.returnValue('url');
    expect(service.sseApiUrl('/path')).toBe('url');
    expect(configuration.url).toHaveBeenCalledWith('backendApiUrl', '/sse/path');
  });

  it('should return sseApiUrl no param', () => {
    configuration.url.and.returnValue('url');
    expect(service.sseApiUrl()).toBe('url');
    expect(configuration.url).toHaveBeenCalledWith('backendApiUrl', '/sse');
  });

  it('should return channels no param', () => {
    configuration.value.and.returnValue(['CHANNEL']);
    expect(service.sseChannels()).toEqual(['CHANNEL']);
    expect(configuration.value).toHaveBeenCalledWith('sseChannels');
  });
});
