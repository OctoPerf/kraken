import {TestBed} from '@angular/core/testing';

import {DockerService} from './docker.service';
import {HttpTestingController} from '@angular/common/http/testing';
import {DialogService} from 'projects/dialog/src/lib/dialog.service';
import {CoreTestModule} from 'projects/commons/src/lib/core/core.module.spec';
import {dialogsServiceSpy} from 'projects/dialog/src/lib/dialog.service.spec';
import {DockerImage} from 'projects/docker/src/lib/entities/docker-image';
import {cold} from 'jasmine-marbles';
import {DockerContainer} from 'projects/docker/src/lib/entities/docker-container';
import {EventSourceService} from 'projects/tools/src/lib/event-source.service';
import {eventSourceServiceSpy} from 'projects/tools/src/lib/event-source.service.spec';
import {BehaviorSubject} from 'rxjs';
import {DockerConfigurationService} from 'projects/docker/src/lib/docker-configuration.service';
import {dockerConfigurationServiceSpy} from 'projects/docker/src/lib/docker-configuration.service.spec';

export const testDockerImage: () => DockerImage = () => {
  return {id: 'id0', name: 'octoperf/kraken', tag: 'latest', created: 'created', size: 42, full: {}};
};

export const testDockerImages: () => DockerImage[] = () => {
  return [
    testDockerImage(),
    {id: 'id1', name: 'name', tag: 'tag', created: 'created', size: 42, full: {}},
  ];
};

export const testDockerContainer: () => DockerContainer = () => {
  return {id: 'id0', name: 'name0', image: 'image0', status: 'created0', full: {}};
};

export const testDockerContainers: () => DockerContainer[] = () => {
  return [
    testDockerContainer(),
    {id: 'id1', name: 'name1', image: 'image1', status: 'created1', full: {}},
  ];
};

export const dockerServiceSpy = () => {
  const spy = jasmine.createSpyObj('DockerService', [
    'images',
    'rmi',
    'pull',
    'ps',
    'rm',
    'tail',
    'logs',
    'start',
    'stop',
    'run',
    'prune',
  ]);
  spy.images.and.returnValue(cold('---x|', {x: testDockerImages()}));
  spy.ps.and.returnValue(cold('---x|', {x: testDockerContainers()}));
  spy.containersSubject = new BehaviorSubject([]);
  spy.imagesSubject = new BehaviorSubject([]);
  return spy;
};


describe('DockerService', () => {
  let service: DockerService;
  let httpTestingController: HttpTestingController;
  let dialogs: DialogService;
  let images: DockerImage[];
  let image: DockerImage;
  let containers: DockerContainer[];
  let container: DockerContainer;

  beforeEach(() => {
    const config = dockerConfigurationServiceSpy();

    TestBed.configureTestingModule({
      imports: [CoreTestModule],
      providers: [
        {provide: DockerConfigurationService, useValue: config},
        {provide: DialogService, useValue: dialogsServiceSpy()},
        {provide: EventSourceService, useValue: eventSourceServiceSpy()},
        DockerService,
      ]
    });

    dialogs = TestBed.get(DialogService);
    service = TestBed.get(DockerService);
    httpTestingController = TestBed.get(HttpTestingController);
    images = testDockerImages();
    image = images[0];
    containers = testDockerContainers();
    container = containers[0];
  });

  afterEach(() => {
    httpTestingController.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should list images', () => {
    service.images().subscribe(value => expect(value).toBe(images), () => fail('list failed'));
    const request = httpTestingController.expectOne('dockerApiUrl/image');
    expect(request.request.method).toBe('GET');
    request.flush(images);
    expect(service.imagesSubject.value).toBe(images);
  });

  it('should rmi', () => {
    service.imagesSubject.next(images);
    service.rmi(image).subscribe(value => expect(value).toBeTruthy(), () => fail('rmi failed'));
    const request = httpTestingController.expectOne(req => req.url === 'dockerApiUrl/image');
    expect(request.request.method).toBe('DELETE');
    expect(request.request.params.get('imageId')).toBe(image.id);
    request.flush('true');
    expect(service.imagesSubject.value.length).toBe(1);
  });

  it('should rmi false', () => {
    service.imagesSubject.next(images);
    service.rmi(image).subscribe(value => expect(value).toBeFalsy(), () => fail('rmi failed'));
    const request = httpTestingController.expectOne(req => req.url === 'dockerApiUrl/image');
    expect(request.request.method).toBe('DELETE');
    expect(request.request.params.get('imageId')).toBe(image.id);
    request.flush('false');
    expect(service.imagesSubject.value.length).toBe(2);
  });

  it('should list containers', () => {
    service.ps().subscribe(value => expect(value).toBe(containers), () => fail('list failed'));
    const request = httpTestingController.expectOne('dockerApiUrl/container/ps');
    expect(request.request.method).toBe('GET');
    request.flush(containers);
    expect(service.containersSubject.value).toEqual(containers);
  });

  it('should rm', () => {
    service.containersSubject.next(containers);
    service.rm(container).subscribe(value => expect(value).toBeTruthy(), () => fail('rm failed'));
    const request = httpTestingController.expectOne(req => req.url === 'dockerApiUrl/container');
    expect(request.request.method).toBe('DELETE');
    expect(request.request.params.get('containerId')).toBe(container.id);
    request.flush('true');
    expect(service.containersSubject.value.length).toBe(1);
  });

  it('should rm false', () => {
    service.containersSubject.next(containers);
    service.rm(container).subscribe(value => expect(value).toBeFalsy(), () => fail('rm failed'));
    const request = httpTestingController.expectOne(req => req.url === 'dockerApiUrl/container');
    expect(request.request.method).toBe('DELETE');
    expect(request.request.params.get('containerId')).toBe(container.id);
    request.flush('false');
    expect(service.containersSubject.value.length).toBe(2);
  });

  it('should pull image', () => {
    const imageName = 'image';
    service.pull(imageName).subscribe();
    const request = httpTestingController.expectOne(req => req.url === 'dockerApiUrl/image/pull');
    expect(request.request.method).toBe('GET');
    expect(request.request.params.get('image')).toBe(imageName);
    request.flush('commandId');
  });

  it('should logs', () => {
    service.logs(container).subscribe();
    const request = httpTestingController.expectOne(req => req.url === 'dockerApiUrl/container/logs');
    expect(request.request.method).toBe('GET');
    expect(request.request.params.get('containerId')).toBe(container.name);
    request.flush('commandId');
  });

  it('should tail', () => {
    service.tail(container).subscribe(value => expect(value).toBeTruthy(), () => fail('tail failed'));
    const request = httpTestingController.expectOne(req => req.url === 'dockerApiUrl/container/tail');
    expect(request.request.method).toBe('GET');
    expect(request.request.params.get('containerId')).toBe(container.id);
    expect(request.request.params.get('lines')).toBe('3');
    request.flush('tail');
  });

  it('should start', () => {
    service.start(container).subscribe();
    const request = httpTestingController.expectOne(req => req.url === 'dockerApiUrl/container/start');
    expect(request.request.method).toBe('POST');
    expect(request.request.params.get('containerId')).toBe(container.id);
    request.flush('true');
    const ps = httpTestingController.expectOne('dockerApiUrl/container');
    expect(ps.request.method).toBe('GET');
    ps.flush(containers);
    expect(service.containersSubject.value).toEqual(containers);
  });

  it('should stop', () => {
    service.stop(container).subscribe();
    const request = httpTestingController.expectOne(req => req.url === 'dockerApiUrl/container/stop');
    expect(request.request.method).toBe('DELETE');
    expect(request.request.params.get('containerId')).toBe(container.id);
    request.flush('true');
    const ps = httpTestingController.expectOne('dockerApiUrl/container');
    expect(ps.request.method).toBe('GET');
    ps.flush(containers);
    expect(service.containersSubject.value).toEqual(containers);
  });

  it('should run', () => {
    service.run('name', 'config').subscribe();
    const request = httpTestingController.expectOne(req => req.url === 'dockerApiUrl/container/run');
    expect(request.request.method).toBe('POST');
    expect(request.request.params.get('name')).toBe('name');
    expect(request.request.body).toBe('config');
    request.flush('containerId');
    const logs = httpTestingController.expectOne(req => req.url === 'dockerApiUrl/container/logs');
    expect(logs.request.method).toBe('GET');
    expect(logs.request.params.get('containerId')).toBe('name');
    logs.flush('commandId');
  });

  it('should prune', () => {
    service.prune(true, false).subscribe();
    const request = httpTestingController.expectOne(req => req.url === 'dockerApiUrl/system/prune');
    expect(request.request.method).toBe('GET');
    expect(request.request.params.get('all')).toBe('true');
    expect(request.request.params.get('volumes')).toBe('false');
    request.flush('containerId');
  });
});
