import {ComponentFixture, TestBed, waitForAsync} from '@angular/core/testing';

import {ImportProjectComponent} from './import-project.component';
import {ActivatedRoute, Router} from '@angular/router';
import {ProjectService} from 'projects/project/src/app/project/project.service';
import {activatedRouteSpy, routerSpy} from 'projects/commons/src/lib/mock/router.mock.spec';
import {projectServiceSpy} from 'projects/project/src/app/project/project.service.spec';
import {VendorsModule} from 'projects/vendors/src/lib/vendors.module';
import {applicationInputComponentSpy} from 'projects/project/src/app/project/application-input/application-input.component.spec';
import {projectNameInputComponentSpy} from 'projects/project/src/app/project/project-name-input/project-name-input.component.spec';
import {of} from 'rxjs';
import {testProject} from 'projects/project/src/app/project/entities/project.spec';
import {repositoryUrlInputComponentSpy} from 'projects/git/src/lib/git-project/repository-url-input/repository-url-input.component.spec';
import {DefaultDialogService} from 'projects/dialog/src/lib/default-dialogs/default-dialog.service';
import {defaultDialogServiceSpy} from 'projects/dialog/src/lib/default-dialogs/default-dialog.service.spec';
import SpyObj = jasmine.SpyObj;

describe('ImportProjectComponent', () => {
  let component: ImportProjectComponent;
  let fixture: ComponentFixture<ImportProjectComponent>;
  let router: SpyObj<Router>;
  let activatedRoute: SpyObj<ActivatedRoute>;
  let projectService: SpyObj<ProjectService>;
  let dialogs: SpyObj<DefaultDialogService>;

  beforeEach(waitForAsync(() => {
    router = routerSpy();
    activatedRoute = activatedRouteSpy();
    projectService = projectServiceSpy();
    dialogs = defaultDialogServiceSpy();

    TestBed.configureTestingModule({
      declarations: [ImportProjectComponent],
      imports: [VendorsModule],
      providers: [
        {provide: Router, useValue: router},
        {provide: ActivatedRoute, useValue: activatedRoute},
        {provide: ProjectService, useValue: projectService},
        {provide: DefaultDialogService, useValue: dialogs},
      ]
    })
      .overrideTemplate(ImportProjectComponent, '')
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ImportProjectComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should create', () => {
    dialogs.waitFor.and.callFake(param => param);
    component.applicationId = applicationInputComponentSpy();
    component.projectName = projectNameInputComponentSpy();
    component.repositoryUrl = repositoryUrlInputComponentSpy();
    component.applicationId.applicationId.setValue('applicationId');
    component.projectName.projectName.setValue('projectName');
    component.repositoryUrl.repositoryUrl.setValue('repositoryUrl');
    projectService.importFromGit.and.returnValue(of(testProject()));
    component.create();
    expect(router.navigate).toHaveBeenCalledWith(['..'], {relativeTo: activatedRoute});
    expect(projectService.importFromGit).toHaveBeenCalledWith('projectName', 'applicationId', 'repositoryUrl');
  });
});
