import {TestBed} from '@angular/core/testing';

import {ConfigurationService} from 'projects/commons/src/lib/config/configuration.service';
import {configurationServiceMock} from 'projects/commons/src/lib/config/configuration.service.spec';
import {
  DOCKER_COMPOSE_ROOT,
  DOCKER_ROOT,
  DockerEndpointService
} from 'projects/docker/src/lib/docker-endpoint.service';

export const dockerEndpointServiceSpy = () => {
  const spy = jasmine.createSpyObj('DockerEndpointService', [
    'dockerPath',
    'dockerComposePath',
  ]);
  spy.dockerPath.and.callFake((path: string) => 'executorApiUrl/docker' + path);
  spy.dockerRoot = '/docker';
  spy.dockerComposePath.and.callFake((path: string) => 'executorApiUrl/docker-compose' + path);
  spy.dockerComposeRoot = '/docker-compose';
  return spy;
};

describe('DockerEndpointService', () => {
  let service: DockerEndpointService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        DockerEndpointService,
        {provide: DOCKER_ROOT, useValue: '/docker'},
        {provide: DOCKER_COMPOSE_ROOT, useValue: '/docker-compose'},
        {provide: ConfigurationService, useValue: configurationServiceMock()}
      ]
    });
    service = TestBed.get(DockerEndpointService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should return docker path', () => {
    expect(service.dockerPath('/suffix')).toBe('executorApiUrl/docker/suffix');
  });

  it('should return docker compose path', () => {
    expect(service.dockerComposePath('/suffix')).toBe('executorApiUrl/docker-compose/suffix');
  });

  it('should return dockerRoot', () => {
    expect(service.dockerRoot).toBe('/docker');
  });

  it('should return dockerComposeRoot', () => {
    expect(service.dockerComposeRoot).toBe('/docker-compose');
  });

});
