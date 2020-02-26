import {TestBed} from '@angular/core/testing';

import {RuntimeHostService} from './runtime-host.service';
import {cold} from 'jasmine-marbles';
import {Host} from 'projects/runtime/src/lib/entities/host';
import {HttpTestingController} from '@angular/common/http/testing';
import {CoreTestModule} from 'projects/commons/src/lib/core/core.module.spec';
import {runtimeConfigurationServiceSpy} from 'projects/runtime/src/lib/runtime-configuration.service.spec';
import {RuntimeConfigurationService} from 'projects/runtime/src/lib/runtime-configuration.service';
import {BehaviorSubject} from 'rxjs';
import {testHost, testHosts} from 'projects/runtime/src/lib/entities/host.spec';

export const runtimeHostServiceSpy = () => {
  const spy = jasmine.createSpyObj('RuntimeHostService', [
    'hosts',
    'all',
    'attach',
    'detach',
    'host',
  ]);
  spy.hosts.and.returnValue(cold('---x|', {x: testHosts()}));
  spy.all.and.returnValue(cold('---x|', {x: testHosts()}));
  spy.hostsSubject = new BehaviorSubject([]);
  spy.allSubject = new BehaviorSubject([]);
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

    service = TestBed.inject(RuntimeHostService);
    httpTestingController = TestBed.inject(HttpTestingController);
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

  it('should list all', () => {
    service.all().subscribe(value => expect(value).toBe(hosts), () => fail('list failed'));
    const request = httpTestingController.expectOne('hostApiUrl/host/all');
    expect(request.request.method).toBe('GET');
    request.flush(hosts);
    expect(service.allSubject.value).toBe(hosts);
  });

  it('should attach', () => {
    const newId = 'newId';
    const host = testHost();
    const updated = testHost();
    updated.id = newId;
    service.attach(host, newId).subscribe(value => expect(value).toEqual(updated), () => fail('attach failed'));
    const request = httpTestingController.expectOne(req => req.url === 'hostApiUrl/host/attach');
    expect(request.request.method).toBe('POST');
    expect(request.request.body).toEqual(updated);
    request.flush(updated);
    expect(service.allSubject.value.length).toBe(0);
  });

  it('should detach', () => {
    const newId = '';
    const host = testHost();
    service.allSubject.next([host]);
    const updated = testHost();
    updated.id = newId;
    service.detach(host).subscribe(value => expect(value).toEqual(updated), () => fail('detach failed'));
    const request = httpTestingController.expectOne(req => req.url === 'hostApiUrl/host/detach');
    expect(request.request.method).toBe('POST');
    expect(request.request.body).toEqual(host);
    request.flush(updated);
    expect(service.allSubject.value).toEqual([updated]);
  });

});
