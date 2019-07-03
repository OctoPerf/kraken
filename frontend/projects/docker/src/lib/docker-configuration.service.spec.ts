import {TestBed} from '@angular/core/testing';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {ConfigurationService} from 'projects/commons/src/lib/config/configuration.service';
import {DockerConfigurationService} from 'projects/docker/src/lib/docker-configuration.service';
import SpyObj = jasmine.SpyObj;
import {configurationServiceSpy} from 'projects/commons/src/lib/config/configuration.service.spec';

export const dockerConfigurationServiceSpy = () => {
  const spy = jasmine.createSpyObj('DockerConfigurationService', [
    'dockerContainerApiUrl',
    'dockerImageApiUrl',
    'dockerSystemApiUrl',
  ]);
  spy.dockerContainerApiUrl.and.callFake((path = '') => `dockerApiUrl/container${path}`);
  spy.dockerImageApiUrl.and.callFake((path = '') => `dockerApiUrl/image${path}`);
  spy.dockerSystemApiUrl.and.callFake((path) => `dockerApiUrl/system${path}`);
  return spy;
};

describe('DockerConfigurationService', () => {
  let service: DockerConfigurationService;
  let configuration: SpyObj<ConfigurationService>;

  beforeEach(() => {
    configuration = configurationServiceSpy();
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [
        DockerConfigurationService,
        {
          provide: ConfigurationService,
          useValue: configuration,
        },
      ]
    });
    service = TestBed.get(DockerConfigurationService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should return dockerContainerApiUrl', () => {
    configuration.url.and.returnValue('url');
    expect(service.dockerContainerApiUrl('/path')).toBe('url');
    expect(configuration.url).toHaveBeenCalledWith('dockerApiUrl', '/container/path');
  });

  it('should return dockerImageApiUrl', () => {
    configuration.url.and.returnValue('url');
    expect(service.dockerImageApiUrl('/path')).toBe('url');
    expect(configuration.url).toHaveBeenCalledWith('dockerApiUrl', '/image/path');
  });

  it('should return dockerSystemApiUrl', () => {
    configuration.url.and.returnValue('url');
    expect(service.dockerSystemApiUrl('/path')).toBe('url');
    expect(configuration.url).toHaveBeenCalledWith('dockerApiUrl', '/system/path');
  });

});
