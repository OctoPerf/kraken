import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {WorkspaceComponent} from './workspace.component';
import {GatlingConfigurationService} from 'projects/gatling/src/app/gatling-configuration.service';
import {gatlingConfigurationServiceSpy} from 'projects/gatling/src/app/gatling-configuration.service.spec';
import {GitProjectService} from 'projects/git/src/lib/git-project/git-project.service';
import {gitProjectServiceSpy} from 'projects/git/src/lib/git-project/git-project.service.spec';
import SpyObj = jasmine.SpyObj;
import {CurrentProjectService} from 'projects/git/src/lib/git-project/current-project/current-project.service';
import {currentProjectServiceSpy} from 'projects/git/src/lib/git-project/current-project/current-project.service.spec';
import {testProject} from 'projects/project/src/app/project/entities/project.spec';

describe('WorkspaceComponent', () => {
  let component: WorkspaceComponent;
  let fixture: ComponentFixture<WorkspaceComponent>;
  let currentProject: SpyObj<CurrentProjectService>;

  beforeEach(async(() => {
    currentProject = currentProjectServiceSpy();
    TestBed.configureTestingModule({
      declarations: [WorkspaceComponent],
      providers: [
        {provide: GatlingConfigurationService, useValue: gatlingConfigurationServiceSpy()},
        {provide: GitProjectService, useValue: gitProjectServiceSpy()},
        {provide: CurrentProjectService, useValue: currentProject},
      ]
    })
      .overrideTemplate(WorkspaceComponent, '')
      .compileComponents();
    currentProject.currentProject.next(testProject());
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(WorkspaceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
