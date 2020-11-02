import {ComponentFixture, TestBed, waitForAsync} from '@angular/core/testing';

import {ProjectItemComponent} from './project-item.component';
import {ProjectService} from 'projects/project/src/app/project/project.service';
import {projectServiceSpy} from 'projects/project/src/app/project/project.service.spec';
import {of} from 'rxjs';
import {testProject} from 'projects/project/src/app/project/entities/project.spec';
import SpyObj = jasmine.SpyObj;

describe('ProjectItemComponent', () => {
  let component: ProjectItemComponent;
  let fixture: ComponentFixture<ProjectItemComponent>;
  let projectService: SpyObj<ProjectService>;

  beforeEach(waitForAsync(() => {
    projectService = projectServiceSpy();
    TestBed.configureTestingModule({
      declarations: [ProjectItemComponent],
      providers: [
        {provide: ProjectService, useValue: projectService},
      ]
    })
      .overrideTemplate(ProjectItemComponent, '')
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProjectItemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should set/get project', () => {
    const project = testProject();
    component.hover = true;
    component.project = project;
    expect(component.hover).toBeFalse();
    expect(component.project).toBe(project);
  });

  it('should delete', () => {
    projectService.deleteProject.and.returnValue(of('true'));
    const project = testProject();
    component.project = project;
    component.delete(true);
    expect(projectService.deleteProject).toHaveBeenCalledWith(project, true);
  });

  it('should open', () => {
    const project = testProject();
    projectService.updateProject.and.returnValue(of(project));
    component.project = project;
    component.open();
    expect(projectService.openProject).toHaveBeenCalledWith(project.applicationId, project.id);
  });
});
