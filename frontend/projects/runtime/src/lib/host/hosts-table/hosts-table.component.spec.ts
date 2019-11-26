import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {HostsTableComponent} from './hosts-table.component';
import {HostService} from 'projects/runtime/src/lib/host/host.service';
import {CoreTestModule} from 'projects/commons/src/lib/core/core.module.spec';
import {hostServiceSpy, testHosts} from 'projects/runtime/src/lib/host/host.service.spec';
import {of} from 'rxjs';
import SpyObj = jasmine.SpyObj;
import {DockerService} from 'projects/docker/src/lib/docker-client/docker.service';

describe('HostsTableComponent', () => {
  let component: HostsTableComponent;
  let fixture: ComponentFixture<HostsTableComponent>;
  let hostService: SpyObj<HostService>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [CoreTestModule],
      declarations: [HostsTableComponent],
      providers: [
        {provide: HostService, useValue: hostServiceSpy()},
      ]
    })
      .overrideTemplate(HostsTableComponent, '')
      .compileComponents();

    hostService = TestBed.get(HostService);
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(HostsTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  afterEach(() => {
    component.ngOnDestroy();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should init', () => {
    spyOn(component, 'refresh');
    component.ngOnInit();
    expect(component.refresh).toHaveBeenCalled();
  });

  it('should refresh', () => {
    hostService.hosts.and.returnValue(of(testHosts()));
    component.refresh();
    expect(component.loading).toBe(true);
    expect(hostService.hosts).toHaveBeenCalled();
  });

  it('should set images', () => {
    const hosts = testHosts();
    hostService.hostsSubject.next(hosts);
    expect(component.dataSource.data).toBe(hosts);
  });

});
