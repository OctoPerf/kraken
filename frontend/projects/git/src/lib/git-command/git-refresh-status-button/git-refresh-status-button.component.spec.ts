import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {GitRefreshStatusButtonComponent} from './git-refresh-status-button.component';
import SpyObj = jasmine.SpyObj;
import {GitCommandService} from 'projects/git/src/lib/git-command/git-command.service';
import {gitCommandServiceSpy} from 'projects/git/src/lib/git-command/git-command.service.spec';
import {of} from 'rxjs';
import {testGitStatus} from 'projects/git/src/lib/entities/git-status.spec';

describe('GitRefreshStatusButtonComponent', () => {
  let component: GitRefreshStatusButtonComponent;
  let fixture: ComponentFixture<GitRefreshStatusButtonComponent>;
  let gitCommand: SpyObj<GitCommandService>;

  beforeEach(async(() => {
    gitCommand = gitCommandServiceSpy();
    TestBed.configureTestingModule({
      declarations: [GitRefreshStatusButtonComponent],
      providers: [
        {provide: GitCommandService, useValue: gitCommand}
      ]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(GitRefreshStatusButtonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should refresh', () => {
    gitCommand.status.and.returnValue(of(testGitStatus()));
    component.refresh();
    expect(gitCommand.status).toHaveBeenCalled();
  });
});
