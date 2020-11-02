import {ComponentFixture, TestBed, waitForAsync} from '@angular/core/testing';
import * as _ from 'lodash';
import {HostsSelectorComponent} from './hosts-selector.component';
import {RuntimeHostService} from 'projects/runtime/src/lib/runtime-host/runtime-host.service';
import {runtimeHostServiceSpy} from 'projects/runtime/src/lib/runtime-host/runtime-host.service.spec';
import {LocalStorageService} from 'projects/tools/src/lib/local-storage.service';
import {localStorageServiceSpy} from 'projects/tools/src/lib/local-storage.service.spec';
import {FormGroup} from '@angular/forms';
import {of} from 'rxjs';
import {testHost, testHosts} from 'projects/runtime/src/lib/entities/host.spec';
import {MatTableDataSource} from '@angular/material/table';
import SpyObj = jasmine.SpyObj;

describe('HostsSelectorComponent', () => {
  let component: HostsSelectorComponent;
  let fixture: ComponentFixture<HostsSelectorComponent>;
  let hostService: SpyObj<RuntimeHostService>;
  let storage: SpyObj<LocalStorageService>;

  beforeEach(waitForAsync(() => {
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

  it('should load hosts', waitForAsync(() => {
    const hosts = testHosts();
    const ids = _.map(hosts, 'id');
    hostService.hosts.and.returnValue(of(hosts));
    storage.getItem.and.returnValue(ids);
    component.ngOnInit();
    expect(component.hosts.value).toEqual(ids);
  }));

  it('should load host', waitForAsync(() => {
    const hosts = testHosts();
    const ids = _.map(hosts, 'id');
    hostService.hosts.and.returnValue(of(hosts));
    storage.getItem.and.returnValue(ids);
    component.multiple = false;
    component.ngOnInit();
    expect(component.hosts.value).toBe(ids[0]);
  }));

  it('should get hostIds multiple', () => {
    const hosts = testHosts();
    const ids = _.map(hosts, 'id');
    component.hosts.setValue(ids);
    expect(component.hostIds).toEqual(ids);
  });

  it('should get hostIds', () => {
    component.multiple = false;
    component.hosts.setValue('id');
    expect(component.hostIds).toEqual(['id']);
  });

  it('should get hostId', () => {
    component.multiple = false;
    component.hosts.setValue('id');
    expect(component.hostId).toBe('id');
  });

  it('should isAllSelected', () => {
    const hosts = testHosts();
    component.selection.select(...hosts);
    component.dataSource = new MatTableDataSource(hosts);
    expect(component.isAllSelected()).toBeTrue();
  });

  it('should not isAllSelected', () => {
    const hosts = testHosts();
    component.selection.select(...hosts);
    component.dataSource = new MatTableDataSource([testHost()]);
    expect(component.isAllSelected()).toBeFalse();
  });

  it('should masterToggle clear', () => {
    const hosts = testHosts();
    component.selection.select(...hosts);
    component.dataSource = new MatTableDataSource(hosts);
    component.masterToggle();
    expect(component.selection.selected.length).toBe(0);
  });

  it('should masterToggle select all', () => {
    const hosts = testHosts();
    component.selection.clear();
    component.dataSource = new MatTableDataSource(hosts);
    component.masterToggle();
    expect(component.selection.selected.length).toBe(hosts.length);
  });
});
