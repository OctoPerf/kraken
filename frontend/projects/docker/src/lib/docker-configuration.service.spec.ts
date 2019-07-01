import {TestBed} from '@angular/core/testing';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {ConfigurationService} from 'projects/commons/src/lib/config/configuration.service';
import {DockerConfigurationService} from 'projects/docker/src/lib/docker-configuration.service';
import SpyObj = jasmine.SpyObj;
import {configurationServiceSpy} from 'projects/commons/src/lib/config/configuration.service.spec';

export const dockerConfigurationServiceSpy = () => {
  const spy = jasmine.createSpyObj('DockerConfigurationService', [
    'dockerApiUrl',
  ]);
  spy.dockerApiUrl.and.callFake((path) => 'dockerApiUrl/docker' + path);
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

  it('should return staticApiUrl', () => {
    configuration.url.and.returnValue('url');
    expect(service.dockerApiUrl('path')).toBe('url');
  });

});
