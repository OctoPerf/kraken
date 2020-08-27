import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {CreateProjectComponent} from './create-project.component';
import SpyObj = jasmine.SpyObj;
import {ActivatedRoute, Router} from '@angular/router';
import {activatedRouteSpy, routerSpy} from 'projects/commons/src/lib/mock/router.mock.spec';
import {ProjectConfigurationService} from 'projects/project/src/app/project/project-configuration.service';
import {ProjectService} from 'projects/project/src/app/project/project.service';
import {projectConfigurationServiceSpy} from 'projects/project/src/app/project/project-configuration.service.spec';
import {projectServiceSpy} from 'projects/project/src/app/project/project.service.spec';
import {VendorsModule} from 'projects/vendors/src/lib/vendors.module';
import {testProject} from 'projects/project/src/app/project/entities/project.spec';
import {of} from 'rxjs';

describe('CreateProjectComponent', () => {
  let component: CreateProjectComponent;
  let fixture: ComponentFixture<CreateProjectComponent>;
  let router: SpyObj<Router>;
  let activatedRoute: SpyObj<ActivatedRoute>;
  let projectConfiguration: SpyObj<ProjectConfigurationService>;
  let projectService: SpyObj<ProjectService>;

  beforeEach(async(() => {
    router = routerSpy();
    activatedRoute = activatedRouteSpy();
    projectConfiguration = projectConfigurationServiceSpy();
    projectService = projectServiceSpy();

    TestBed.configureTestingModule({
      declarations: [CreateProjectComponent],
      imports: [VendorsModule],
      providers: [
        {provide: Router, useValue: router},
        {provide: ActivatedRoute, useValue: activatedRoute},
        {provide: ProjectConfigurationService, useValue: projectConfiguration},
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

  it('should return projectName', () => {
    expect(component.projectName.value).toBe('');
  });

  it('should return applicationId', () => {
    expect(component.applicationId.value).toBe('gatling');
  });

  it('should create', () => {
    projectService.createProject.and.returnValue(of(testProject()));
    component.create();
    expect(router.navigate).toHaveBeenCalledWith(['..'], {relativeTo: activatedRoute});
  });
});
