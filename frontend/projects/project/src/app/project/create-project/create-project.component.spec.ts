import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {CreateProjectComponent} from './create-project.component';
import {ActivatedRoute, Router} from '@angular/router';
import {activatedRouteSpy, routerSpy} from 'projects/commons/src/lib/mock/router.mock.spec';
import {ProjectService} from 'projects/project/src/app/project/project.service';
import {projectServiceSpy} from 'projects/project/src/app/project/project.service.spec';
import {VendorsModule} from 'projects/vendors/src/lib/vendors.module';
import {testProject} from 'projects/project/src/app/project/entities/project.spec';
import {of} from 'rxjs';
import {applicationInputComponentSpy} from 'projects/project/src/app/project/application-input/application-input.component.spec';
import {projectNameInputComponentSpy} from 'projects/project/src/app/project/project-name-input/project-name-input.component.spec';
import SpyObj = jasmine.SpyObj;

describe('CreateProjectComponent', () => {
  let component: CreateProjectComponent;
  let fixture: ComponentFixture<CreateProjectComponent>;
  let router: SpyObj<Router>;
  let activatedRoute: SpyObj<ActivatedRoute>;
  let projectService: SpyObj<ProjectService>;

  beforeEach(async(() => {
    router = routerSpy();
    activatedRoute = activatedRouteSpy();
    projectService = projectServiceSpy();

    TestBed.configureTestingModule({
      declarations: [CreateProjectComponent],
      imports: [VendorsModule],
      providers: [
        {provide: Router, useValue: router},
        {provide: ActivatedRoute, useValue: activatedRoute},
        {provide: ProjectService, useValue: projectService},
      ]
    })
      .overrideTemplate(CreateProjectComponent, '')
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CreateProjectComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should create', () => {
    component.applicationId = applicationInputComponentSpy();
    component.projectName = projectNameInputComponentSpy();
    component.applicationId.applicationId.setValue('applicationId');
    component.projectName.projectName.setValue('projectName');
    projectService.createProject.and.returnValue(of(testProject()));
    component.create();
    expect(router.navigate).toHaveBeenCalledWith(['..'], {relativeTo: activatedRoute});
    expect(projectService.createProject).toHaveBeenCalledWith('projectName', 'applicationId');
  });
});
