import {TestBed} from '@angular/core/testing';

import {HostService} from './host.service';
import {DockerContainer} from 'projects/docker/src/lib/entities/docker-container';
import {cold} from 'jasmine-marbles';
import {Host} from 'projects/runtime/src/lib/entities/host';
import {DockerService} from 'projects/docker/src/lib/docker-client/docker.service';
import {HttpTestingController} from '@angular/common/http/testing';
import {DialogService} from 'projects/dialog/src/lib/dialog.service';
import {DockerImage} from 'projects/docker/src/lib/entities/docker-image';
import {dockerConfigurationServiceSpy} from 'projects/docker/src/lib/docker-configuration.service.spec';
import {CoreTestModule} from 'projects/commons/src/lib/core/core.module.spec';
import {DockerConfigurationService} from 'projects/docker/src/lib/docker-configuration.service';
import {dialogsServiceSpy} from 'projects/dialog/src/lib/dialog.service.spec';
import {EventSourceService} from 'projects/tools/src/lib/event-source.service';
import {eventSourceServiceSpy} from 'projects/tools/src/lib/event-source.service.spec';
import {testDockerContainers, testDockerImages} from 'projects/docker/src/lib/docker-client/docker.service.spec';
import {runtimeConfigurationServiceSpy} from 'projects/runtime/src/lib/runtime-configuration.service.spec';
import {RuntimeConfigurationService} from 'projects/runtime/src/lib/runtime-configuration.service';
import {BehaviorSubject} from 'rxjs';

export const testHost: () => Host = () => {
  return {id: 'id0', name: 'name0', capacity: {'key': 'value'}, addresses: [{type: 'type', address: 'address'}]};
};

export const testHosts: () => Host[] = () => {
  return [
    testHost(),
    {id: 'id1', name: 'name1', capacity: {}, addresses: []},
  ];
};

export const hostServiceSpy = () => {
  const spy = jasmine.createSpyObj('HostService', [
    'hosts',
  ]);
  spy.hosts.and.returnValue(cold('---x|', {x: testHosts()}));
  spy.hostsSubject = new BehaviorSubject([]);
  return spy;
};

describe('HostService', () => {

  let service: HostService;
  let httpTestingController: HttpTestingController;
  let hosts: Host[];

  beforeEach(() => {
    const config = runtimeConfigurationServiceSpy();

    TestBed.configureTestingModule({
      imports: [CoreTestModule],
      providers: [
        {provide: RuntimeConfigurationService, useValue: config},
        HostService,
      ]
    });

    service = TestBed.get(HostService);
    httpTestingController = TestBed.get(HttpTestingController);
    hosts = testHosts();
  });

  afterEach(() => {
    httpTestingController.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should list', () => {
    service.hosts().subscribe(value => expect(value).toBe(hosts), () => fail('list failed'));
    const request = httpTestingController.expectOne('hostApiUrl/host/list');
    expect(request.request.method).toBe('GET');
    request.flush(hosts);
    expect(service.hostsSubject.value).toBe(hosts);
  });

});
