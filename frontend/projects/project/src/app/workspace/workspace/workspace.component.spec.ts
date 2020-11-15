import {ComponentFixture, TestBed, waitForAsync} from '@angular/core/testing';
import {ProjectService} from 'projects/project/src/app/project/project.service';
import {projectServiceSpy} from 'projects/project/src/app/project/project.service.spec';
import {WorkspaceComponent} from 'projects/project/src/app/workspace/workspace/workspace.component';
import SpyObj = jasmine.SpyObj;

describe('WorkspaceComponent', () => {
  let component: WorkspaceComponent;
  let fixture: ComponentFixture<WorkspaceComponent>;
  let projectService: SpyObj<ProjectService>;

  beforeEach(waitForAsync(() => {
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
