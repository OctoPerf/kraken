import {TestBed} from '@angular/core/testing';

import {RuntimeHostService} from './runtime-host.service';
import {cold} from 'jasmine-marbles';
import {Host} from 'projects/runtime/src/lib/entities/host';
import {HttpTestingController} from '@angular/common/http/testing';
import {CoreTestModule} from 'projects/commons/src/lib/core/core.module.spec';
import {runtimeConfigurationServiceSpy} from 'projects/runtime/src/lib/runtime-configuration.service.spec';
import {RuntimeConfigurationService} from 'projects/runtime/src/lib/runtime-configuration.service';
import {BehaviorSubject} from 'rxjs';
import {testHosts} from 'projects/runtime/src/lib/entities/host.spec';

export const runtimeHostServiceSpy = () => {
  const spy = jasmine.createSpyObj('RuntimeHostService', [
    'hosts',
    'host',
  ]);
  spy.hosts.and.returnValue(cold('---x|', {x: testHosts()}));
  spy.hostsSubject = new BehaviorSubject([]);
  return spy;
};

describe('HostService', () => {

  let service: RuntimeHostService;
  let httpTestingController: HttpTestingController;
  let hosts: Host[];

  beforeEach(() => {
    const config = runtimeConfigurationServiceSpy();

    TestBed.configureTestingModule({
      imports: [CoreTestModule],
      providers: [
        {provide: RuntimeConfigurationService, useValue: config},
        RuntimeHostService,
      ]
    });

    service = TestBed.get(RuntimeHostService);
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
    expect(service.host(hosts[0].id)).toBe(hosts[0]);
  });

});
