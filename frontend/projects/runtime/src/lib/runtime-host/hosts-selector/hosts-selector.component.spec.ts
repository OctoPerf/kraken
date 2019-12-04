import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {HostsSelectorComponent} from './hosts-selector.component';
import {RuntimeHostService} from 'projects/runtime/src/lib/runtime-host/runtime-host.service';
import {runtimeHostServiceSpy} from 'projects/runtime/src/lib/runtime-host/runtime-host.service.spec';
import {LocalStorageService} from 'projects/tools/src/lib/local-storage.service';
import {localStorageServiceSpy} from 'projects/tools/src/lib/local-storage.service.spec';
import {FormControl, FormGroup} from '@angular/forms';
import {of} from 'rxjs';
import {testHosts} from 'projects/runtime/src/lib/entities/host.spec';
import SpyObj = jasmine.SpyObj;

describe('HostsSelectorComponent', () => {
  let component: HostsSelectorComponent;
  let fixture: ComponentFixture<HostsSelectorComponent>;
  let hostService: SpyObj<RuntimeHostService>;
  let storage: SpyObj<LocalStorageService>;

  beforeEach(async(() => {
    hostService = runtimeHostServiceSpy();
    storage = localStorageServiceSpy();
    TestBed.configureTestingModule({
      declarations: [HostsSelectorComponent],
      providers: [
        {provide: RuntimeHostService, useValue: hostService},
        {provide: LocalStorageService, useValue: storage},
      ]
    })
      .overrideTemplate(HostsSelectorComponent, '')
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(HostsSelectorComponent);
    component = fixture.componentInstance;
    component.formGroup = new FormGroup({});
    component.storageId = 'id';
    component.multiple = true;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load hosts', () => {
    const hosts = testHosts();
    hostService.hosts.and.returnValue(of(hosts));
    storage.getItem.and.returnValue([hosts[0].id]);
    component.ngOnInit();
    expect(component.hosts.value).toEqual([hosts[0].id]);
  });

  it('should load host', () => {
    const hosts = testHosts();
    hostService.hosts.and.returnValue(of(hosts));
    storage.getItem.and.returnValue([]);
    component.multiple = false;
    component.ngOnInit();
    expect(component.hosts.value).toBe(hosts[0].id);
  });

  it('should get hostIds', () => {
    expect(component.hostIds).toEqual([]);
    const ids = ['id0', 'id1'];
    component.formGroup.addControl('hosts', new FormControl(ids, []));
    expect(component.hostIds).toBe(ids);
  });

  it('should get hostId', () => {
    expect(component.hostId).toBeNull();
    const id = 'id';
    component.formGroup.addControl('hosts', new FormControl(id, []));
    expect(component.hostId).toBe(id);
  });
});
