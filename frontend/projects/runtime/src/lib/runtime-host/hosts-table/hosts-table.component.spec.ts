import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {HostsTableComponent} from './hosts-table.component';
import {RuntimeHostService} from 'projects/runtime/src/lib/runtime-host/runtime-host.service';
import {CoreTestModule} from 'projects/commons/src/lib/core/core.module.spec';
import {runtimeHostServiceSpy} from 'projects/runtime/src/lib/runtime-host/runtime-host.service.spec';
import {of} from 'rxjs';
import SpyObj = jasmine.SpyObj;
import {testHosts} from 'projects/runtime/src/lib/entities/host.spec';

describe('HostsTableComponent', () => {
  let component: HostsTableComponent;
  let fixture: ComponentFixture<HostsTableComponent>;
  let hostService: SpyObj<RuntimeHostService>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [CoreTestModule],
      declarations: [HostsTableComponent],
      providers: [
        {provide: RuntimeHostService, useValue: runtimeHostServiceSpy()},
      ]
    })
      .overrideTemplate(HostsTableComponent, '')
      .compileComponents();

    hostService = TestBed.inject(RuntimeHostService) as SpyObj<RuntimeHostService>;
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
    hostService.all.and.returnValue(of(testHosts()));
    component.refresh();
    expect(component.loading).toBe(true);
    expect(hostService.all).toHaveBeenCalled();
  });

  it('should set hosts', () => {
    const hosts = testHosts();
    hostService.allSubject.next(hosts);
    expect(component.dataSource.data).toBe(hosts);
  });

});
