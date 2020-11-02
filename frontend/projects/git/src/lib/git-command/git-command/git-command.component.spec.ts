import {ComponentFixture, TestBed, waitForAsync} from '@angular/core/testing';

import {GitCommandComponent} from './git-command.component';
import {GitProjectService} from 'projects/git/src/lib/git-project/git-project.service';
import {GitCommandService} from 'projects/git/src/lib/git-command/git-command.service';
import {gitProjectServiceSpy} from 'projects/git/src/lib/git-project/git-project.service.spec';
import {gitCommandServiceSpy} from 'projects/git/src/lib/git-command/git-command.service.spec';
import {of, throwError} from 'rxjs';
import {codeEditorComponentSpy} from 'projects/editor/src/lib/code-editor/code-editor.component.spec';
import {RestServerError} from 'projects/commons/src/lib/config/rest-server-error';
import SpyObj = jasmine.SpyObj;

describe('GitCommandComponent', () => {
  let component: GitCommandComponent;
  let fixture: ComponentFixture<GitCommandComponent>;
  let gitProject: SpyObj<GitProjectService>;
  let gitCommand: SpyObj<GitCommandService>;

  beforeEach(waitForAsync(() => {
    gitProject = gitProjectServiceSpy();
    gitCommand = gitCommandServiceSpy();
    TestBed.configureTestingModule({
      declarations: [GitCommandComponent],
      providers: [
        {provide: GitProjectService, useValue: gitProject},
        {provide: GitCommandService, useValue: gitCommand},
      ]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(GitCommandComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    component.codeEditor = codeEditorComponentSpy();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should not execute', () => {
    component.loading = true;
    component.execute();
    expect(gitCommand.execute).not.toHaveBeenCalled();
  });

  it('should execute git', () => {
    component.input = 'git status';
    gitCommand.execute.and.returnValue(of(null));
    component.execute();
    expect(component.codeEditor.appendText).toHaveBeenCalledWith('> git status\n');
    expect(gitCommand.execute).toHaveBeenCalledWith('git status');
  });

  it('should execute', () => {
    component.input = 'status';
    gitCommand.execute.and.returnValue(of(null));
    component.execute();
    expect(component.codeEditor.appendText).toHaveBeenCalledWith('> git status\n');
    expect(gitCommand.execute).toHaveBeenCalledWith('git status');
  });

  it('should execute fail', () => {
    component.input = 'status';
    gitCommand.execute.and.returnValue(throwError(new RestServerError('title', 'message', 'trace')));
    component.execute();
    expect(component.codeEditor.appendText).toHaveBeenCalledWith('> git status\n');
    expect(component.codeEditor.appendText).toHaveBeenCalledWith('title: message\ntrace');
  });

  it('should append logs', () => {
    gitCommand.logsSubject.next('log');
    expect(component.codeEditor.appendText).toHaveBeenCalledWith('log\n');
  });
});
