import {TestBed} from '@angular/core/testing';

import {ProjectService} from './project.service';
import {BehaviorSubject, of} from 'rxjs';
import {DefaultDialogService} from 'projects/dialog/src/lib/default-dialogs/default-dialog.service';
import {defaultDialogServiceSpy} from 'projects/dialog/src/lib/default-dialogs/default-dialog.service.spec';
import {WindowService} from 'projects/tools/src/lib/window.service';
import {windowServiceSpy} from 'projects/tools/src/lib/window.service.spec';
import {HttpTestingController} from '@angular/common/http/testing';
import {CoreTestModule} from 'projects/commons/src/lib/core/core.module.spec';
import {ConfigurationService} from 'projects/commons/src/lib/config/configuration.service';
import {configurationServiceSpy} from 'projects/commons/src/lib/config/configuration.service.spec';
import {testProject, testProjects} from 'projects/project/src/app/project/entities/project.spec';
import SpyObj = jasmine.SpyObj;
import * as _ from 'lodash';

export const projectServiceSpy = () => {
  const spy = jasmine.createSpyObj('ProjectService', [
    'listProjects',
    'openProject',
    'updateProject',
    'createProject',
    'deleteProject'
  ]);
  spy.projectsSubject = new BehaviorSubject([]);
  return spy;
};

describe('ProjectService', () => {
  let service: ProjectService;
  let dialogs: SpyObj<DefaultDialogService>;
  let window: SpyObj<WindowService>;
  let configuration: SpyObj<ConfigurationService>;
  let httpTestingController: HttpTestingController;

  beforeEach(() => {
    dialogs = defaultDialogServiceSpy();
    window = windowServiceSpy();
    configuration = configurationServiceSpy();
    TestBed.configureTestingModule({
      imports: [CoreTestModule],
      providers: [
        {provide: DefaultDialogService, useValue: dialogs},
        {provide: WindowService, useValue: window},
        {provide: ConfigurationService, useValue: configuration}
      ]
    });
    service = TestBed.inject(ProjectService);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpTestingController.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should listProjects', () => {
    const projects = testProjects();
    service.listProjects().subscribe(data => expect(data).toBe(projects), () => fail('list failed'));
    const request = httpTestingController.expectOne('projectApiUrl/project/list');
    expect(request.request.method).toBe('GET');
    request.flush(projects);
    expect(service.projectsSubject.value).toEqual(projects);
  });

  it('should delete project', () => {
    dialogs.delete.and.returnValue(of(null));
    const projects = testProjects();
    const length = projects.length;
    service.projectsSubject.next(projects);
    const project = projects[0];
    service.deleteProject(project, true).subscribe(data => expect(data).toBe('ok'), () => fail('delete failed'));
    const request = httpTestingController.expectOne('projectApiUrl/project/delete');
    expect(request.request.method).toBe('POST');
    request.flush('ok');
    expect(service.projectsSubject.value.length).toEqual(length - 1);
  });

  it('should create project', () => {
    const project = testProject();
    service.createProject('name', 'app').subscribe(data => expect(data).toBe(project), () => fail('create failed'));
    const request = httpTestingController.expectOne('projectApiUrl/project?applicationId=app&name=name');
    expect(request.request.method).toBe('POST');
    request.flush(project);
    expect(service.projectsSubject.value).toEqual([project]);
  });

  it('should update project', () => {
    const projects = testProjects();
    service.projectsSubject.next(projects);
    const project = _.cloneDeep(projects[0]);
    project.name = 'newName';
    service.updateProject(project).subscribe(data => expect(data).toBe(project), () => fail('create failed'));
    const request = httpTestingController.expectOne('projectApiUrl/project');
    expect(request.request.method).toBe('PUT');
    request.flush(project);
    expect(service.projectsSubject.value[0]).toEqual(project);
  });

  it('should open project', () => {
    window.url.and.returnValue('url');
    service.openProject('applicationId', 'projectId');
    expect(window.open).toHaveBeenCalled();
  });

});
