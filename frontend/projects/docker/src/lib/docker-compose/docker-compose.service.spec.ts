import {TestBed} from '@angular/core/testing';

import {DockerComposeService} from './docker-compose.service';
import {HttpTestingController} from '@angular/common/http/testing';
import {CoreTestModule} from 'projects/commons/src/lib/core/core.module.spec';
import {EventBusService} from 'projects/event/src/lib/event-bus.service';
import {eventBusSpy} from 'projects/event/src/lib/event-bus.service.spec';
import {DockerConfigurationService} from 'projects/docker/src/lib/docker-configuration.service';
import {dockerConfigurationServiceSpy} from 'projects/docker/src/lib/docker-configuration.service.spec';

export const dockerComposeServiceSpy = () => {
  const spy = jasmine.createSpyObj('DockerComposeService', [
    'command',
  ]);
  return spy;
};


describe('DockerComposeService', () => {

  let service: DockerComposeService;
  let httpTestingController: HttpTestingController;
  let eventBus: EventBusService;

  beforeEach(() => {
    eventBus = eventBusSpy();
    TestBed.configureTestingModule({
      imports: [CoreTestModule],
      providers: [
        DockerComposeService,
        {provide: DockerConfigurationService, useValue: dockerConfigurationServiceSpy()},
        {provide: EventBusService, useValue: eventBus},
      ]
    });

    service = TestBed.get(DockerComposeService);
    httpTestingController = TestBed.get(HttpTestingController);
  });

  afterEach(() => {
    httpTestingController.verify();
  });
  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should up', () => {
    service.command('up', 'path').subscribe();
    const request = httpTestingController.expectOne(req => req.url === 'executorApiUrl/docker-compose/up');
    expect(request.request.method).toBe('GET');
    expect(request.request.params.get('path')).toBe('path');
    request.flush('commandId');
  });

  it('should down', () => {
    service.command('down', 'path').subscribe();
    const request = httpTestingController.expectOne(req => req.url === 'executorApiUrl/docker-compose/down');
    expect(request.request.method).toBe('GET');
    expect(request.request.params.get('path')).toBe('path');
    request.flush('commandId');
  });

});
