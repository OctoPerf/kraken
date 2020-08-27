import {async, ComponentFixture, TestBed} from '@angular/core/testing';
import {ProjectService} from 'projects/project/src/app/project/project.service';
import {projectServiceSpy} from 'projects/project/src/app/project/project.service.spec';
import SpyObj = jasmine.SpyObj;
import {WorkspaceComponent} from 'projects/project/src/app/workspace/workspace/workspace.component';

describe('WorkspaceComponent', () => {
  let component: WorkspaceComponent;
  let fixture: ComponentFixture<WorkspaceComponent>;
  let projectService: SpyObj<ProjectService>;

  beforeEach(async(() => {
    projectService = projectServiceSpy();
    TestBed.configureTestingModule({
      declarations: [WorkspaceComponent],
      providers: [
        {provide: ProjectService, useValue: projectService},
      ]
    })
      .overrideTemplate(WorkspaceComponent, '')
      .compileComponents();
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
