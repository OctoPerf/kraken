import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {ContainersTableComponent} from './containers-table.component';
import {DockerService} from 'projects/docker/src/lib/docker-client/docker.service';
import {
  dockerServiceSpy,
  testDockerContainer,
  testDockerContainers
} from 'projects/docker/src/lib/docker-client/docker.service.spec';
import {DialogService} from 'projects/dialog/src/lib/dialog.service';
import {dialogsServiceSpy} from 'projects/dialog/src/lib/dialog.service.spec';
import {CoreTestModule} from 'projects/commons/src/lib/core/core.module.spec';
import {of} from 'rxjs';
import {DOCKER_ID} from 'projects/docker/src/lib/docker-id';
import {EventBusService} from 'projects/event/src/lib/event-bus.service';
import {eventBusSpy} from 'projects/event/src/lib/event-bus.service.spec';
import {LocalStorageService} from 'projects/tools/src/lib/local-storage.service';
import {localStorageServiceSpy} from 'projects/tools/src/lib/local-storage.service.spec';
import {RunContainerDialogComponent} from 'projects/docker/src/lib/docker-dialogs/run-container-dialog/run-container-dialog.component';
import SpyObj = jasmine.SpyObj;
import {DockerContainer} from 'projects/docker/src/lib/entities/docker-container';
import {SystemPruneDialogComponent} from 'projects/docker/src/lib/docker-dialogs/system-prune-dialog/system-prune-dialog.component';
import {DialogSize} from 'projects/dialog/src/lib/dialog-size';
import {JsonPipe} from '@angular/common';

describe('ContainersTableComponent', () => {
  let component: ContainersTableComponent;
  let fixture: ComponentFixture<ContainersTableComponent>;
  let dockerService: SpyObj<DockerService>;
  let dialogs: SpyObj<DialogService>;
  let eventBus: SpyObj<EventBusService>;
  let localStorage: SpyObj<LocalStorageService>;
  let container: DockerContainer;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [CoreTestModule],
      declarations: [ContainersTableComponent],
      providers: [
        {provide: DockerService, useValue: dockerServiceSpy()},
        {provide: DialogService, useValue: dialogsServiceSpy()},
        {provide: EventBusService, useValue: eventBusSpy()},
        {provide: LocalStorageService, useValue: localStorageServiceSpy()},
        {provide: DOCKER_ID, useValue: 'docker'},
        JsonPipe,
      ]
    })
      .overrideTemplate(ContainersTableComponent, '')
      .compileComponents();

    dockerService = TestBed.get(DockerService);
    dialogs = TestBed.get(DialogService);
    eventBus = TestBed.get(EventBusService);
    localStorage = TestBed.get(LocalStorageService);
    container = testDockerContainer();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ContainersTableComponent);
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
    dockerService.ps.and.returnValue(of([]));
    component.refresh();
    expect(component.loading).toBe(true);
    expect(dockerService.ps).toHaveBeenCalled();
  });

  it('should full', () => {
    component.full(container);
    expect(dialogs.inspect).toHaveBeenCalledWith('Docker Container', '{}', 'ADMIN_INSPECT_CONTAINER');
  });

  it('should rm', () => {
    dialogs.delete.and.returnValue(of(null));
    dockerService.rm.and.returnValue(of(true));
    component.rm(container);
    expect(dialogs.delete).toHaveBeenCalledWith('Docker Container', [`${container.image} (${container.name})`], 'ADMIN_DELETE_CONTAINER');
    expect(dockerService.rm).toHaveBeenCalledWith(container);
  });

  it('should loadTail', () => {
    dockerService.tail.and.returnValue(of('tail'));
    component.loadTail(container);
    component.loadTail(container);
    expect(dockerService.tail).toHaveBeenCalledTimes(1);
  });

  it('should tail loading', () => {
    expect(component.tail(container)).toBe('Loading...');
  });

  it('should tail', () => {
    dockerService.tail.and.returnValue(of('tail'));
    component.loadTail(container);
    expect(component.tail(container)).toBe('tail');
  });

  it('should logs', () => {
    dockerService.logs.and.returnValue(of('commandId'));
    component.logs(container);
    expect(dockerService.logs).toHaveBeenCalled();
  });

  it('should start', () => {
    dockerService.start.and.returnValue(of('true'));
    component.start(container);
    expect(dockerService.start).toHaveBeenCalledWith(container);
  });

  it('should stop', () => {
    dockerService.stop.and.returnValue(of('true'));
    component.stop(container);
    expect(dockerService.stop).toHaveBeenCalledWith(container);
  });

  it('should isUp', () => {
    expect(component.isUp(container)).toBeFalsy();
    expect(component.isUp({status: 'Up test'} as any)).toBeTruthy();
  });

  it('should run', () => {
    dockerService.run.and.returnValue(of('containerId'));
    dialogs.open.and.returnValue(of({name: 'newName', config: 'newConfig'}));
    component.run();
    expect(dialogs.open).toHaveBeenCalledWith(RunContainerDialogComponent, DialogSize.SIZE_MD, {
      name: '',
      config: 'Image: image-name'
    });
    expect(dockerService.run).toHaveBeenCalledWith('newName', 'newConfig');
    expect(localStorage.set).toHaveBeenCalledWith('docker-run-container-name', 'newName');
    expect(localStorage.set).toHaveBeenCalledWith('docker-run-container-config', 'newConfig');
  });

  it('should prune', () => {
    dockerService.prune.and.returnValue(of('ok'));
    dialogs.open.and.returnValue(of({all: true, volumes: false}));
    component.prune();
    expect(dialogs.open).toHaveBeenCalledWith(SystemPruneDialogComponent, DialogSize.SIZE_MD);
    expect(dockerService.prune).toHaveBeenCalledWith(true, false);
  });

  it('should set containers', () => {
    dockerService.containersSubject.next(testDockerContainers());
    expect(component.dataSource).toBeDefined();
    expect(component.loading).toBeFalsy();
    expect(component.tooltips).toEqual({});
  });
});
