import {ComponentFixture, TestBed, waitForAsync} from '@angular/core/testing';

import {ProjectMenuComponent} from 'projects/git/src/lib/git-project/project-menu/project-menu.component';
import {GitProjectService} from 'projects/git/src/lib/git-project/git-project.service';
import {gitCommandServiceSpy} from 'projects/git/src/lib/git-command/git-command.service.spec';
import SpyObj = jasmine.SpyObj;

describe('ProjectMenuComponent', () => {
  let component: ProjectMenuComponent;
  let fixture: ComponentFixture<ProjectMenuComponent>;
  let gitProjectService: SpyObj<GitProjectService>;

  beforeEach(waitForAsync(() => {
    gitProjectService = gitCommandServiceSpy();
    TestBed.configureTestingModule({
      declarations: [ProjectMenuComponent],
      providers: [
        {provide: GitProjectService, useValue: gitProjectService}
      ]
    })
      .overrideTemplate(ProjectMenuComponent, '')
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProjectMenuComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
