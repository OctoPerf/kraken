import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {GitStatusComponent} from './git-status.component';
import {GitProjectService} from 'projects/git/src/lib/git-project/git-project.service';
import {GitCommandService} from 'projects/git/src/lib/git-command/git-command.service';
import SpyObj = jasmine.SpyObj;
import {gitProjectServiceSpy} from 'projects/git/src/lib/git-project/git-project.service.spec';
import {gitCommandServiceSpy} from 'projects/git/src/lib/git-command/git-command.service.spec';
import {testGitStatus} from 'projects/git/src/lib/entities/git-status.spec';

describe('GitStatusComponent', () => {
  let component: GitStatusComponent;
  let fixture: ComponentFixture<GitStatusComponent>;
  let gitProject: SpyObj<GitProjectService>;
  let gitCommand: SpyObj<GitCommandService>;

  beforeEach(async(() => {
    gitProject = gitProjectServiceSpy();
    gitCommand = gitCommandServiceSpy();
    TestBed.configureTestingModule({
      declarations: [GitStatusComponent],
      providers: [
        {provide: GitProjectService, useValue: gitProject},
        {provide: GitCommandService, useValue: gitCommand},
      ]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(GitStatusComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should set status', () => {
    gitCommand.statusSubject.next(testGitStatus());
    expect(component.status).toBeDefined();
  });
});
