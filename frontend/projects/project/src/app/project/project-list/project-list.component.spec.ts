import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {ProjectListComponent} from './project-list.component';
import {ProjectService} from 'projects/project/src/app/project/project.service';
import {projectServiceSpy} from 'projects/project/src/app/project/project.service.spec';
import SpyObj = jasmine.SpyObj;
import {testProjects} from 'projects/project/src/app/project/entities/project.spec';

describe('ProjectListComponent', () => {
  let component: ProjectListComponent;
  let fixture: ComponentFixture<ProjectListComponent>;
  let projectService: SpyObj<ProjectService>;

  beforeEach(async(() => {
    projectService = projectServiceSpy();
    TestBed.configureTestingModule({
      declarations: [ProjectListComponent],
      providers: [
        {provide: ProjectService, useValue: projectService},
      ]
    })
      .overrideTemplate(ProjectListComponent, '')
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProjectListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  afterEach(() => {
    component.ngOnDestroy();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should set projects', () => {
    const projects = testProjects();
    projectService.projectsSubject.next(projects);
    expect(component.projects).toEqual(projects);
  });

  it('should filter projects', () => {
    const projects = testProjects();
    projectService.projectsSubject.next(projects);
    component.filter = 'NaMe0';
    component.filterChanged();
    expect(component.projects.length).toBe(1);
  });
});
