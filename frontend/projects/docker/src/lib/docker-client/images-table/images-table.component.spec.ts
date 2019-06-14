import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {ImagesTableComponent} from './images-table.component';
import {CoreTestModule} from 'projects/commons/src/lib/core/core.module.spec';
import {DockerService} from 'projects/docker/src/lib/docker-client/docker.service';
import {
  dockerServiceSpy,
  testDockerImage,
  testDockerImages
} from 'projects/docker/src/lib/docker-client/docker.service.spec';
import {DialogService} from 'projects/dialog/src/lib/dialog.service';
import {dialogsServiceSpy} from 'projects/dialog/src/lib/dialog.service.spec';
import {of} from 'rxjs';
import {DockerImage} from 'projects/docker/src/lib/entities/docker-image';
import SpyObj = jasmine.SpyObj;
import {JsonPipe} from '@angular/common';

describe('ImagesTableComponent', () => {
  let component: ImagesTableComponent;
  let fixture: ComponentFixture<ImagesTableComponent>;
  let dockerService: SpyObj<DockerService>;
  let dialogs: SpyObj<DialogService>;
  let images: DockerImage[];
  let image: DockerImage;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [CoreTestModule],
      declarations: [ImagesTableComponent],
      providers: [
        {provide: DockerService, useValue: dockerServiceSpy()},
        {provide: DialogService, useValue: dialogsServiceSpy()},
        JsonPipe,
      ]
    })
      .overrideTemplate(ImagesTableComponent, '')
      .compileComponents();

    dockerService = TestBed.get(DockerService);
    dialogs = TestBed.get(DialogService);
    images = testDockerImages();
    image = images[0];
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ImagesTableComponent);
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
    dockerService.images.and.returnValue(of(testDockerImage()));
    component.refresh();
    expect(component.loading).toBe(true);
    expect(dockerService.images).toHaveBeenCalled();
  });

  it('should full', () => {
    component.full(image);
    expect(dialogs.inspect).toHaveBeenCalledWith('Docker Image', '{}', 'ADMIN_INSPECT_IMAGE');
  });

  it('should rmi', () => {
    dialogs.delete.and.returnValue(of(true));
    dockerService.rmi.and.returnValue(of(true));
    component.rmi(image);
    expect(dialogs.delete).toHaveBeenCalledWith('Docker Image', [`${image.name}:${image.tag}`], 'ADMIN_DELETE_IMAGE');
    expect(dockerService.rmi).toHaveBeenCalledWith(image);
  });

  it('should pullImage', () => {
    dialogs.open.and.returnValue(of('name'));
    dockerService.pull.and.returnValue(of('true'));
    component.pullImage();
    expect(dialogs.open).toHaveBeenCalled();
    expect(dockerService.pull).toHaveBeenCalled();
  });

  it('should set images', () => {
    dockerService.imagesSubject.next(images);
    expect(component.dataSource.data).toBe(images);
  });

});
