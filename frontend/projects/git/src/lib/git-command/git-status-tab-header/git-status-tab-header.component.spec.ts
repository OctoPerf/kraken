import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { GitStatusTabHeaderComponent } from './git-status-tab-header.component';
import {SIDE_HEADER_DATA, TAB_HEADER_DATA} from 'projects/tabs/src/lib/tab-header/tab-header.component';
import {newTestTab} from 'projects/tabs/src/lib/tab-header/tab-header.component.spec';
import {TabsSide} from 'projects/tabs/src/lib/tabs-side';
import {Component} from '@angular/core';
import SpyObj = jasmine.SpyObj;
import {GitCommandService} from 'projects/git/src/lib/git-command/git-command.service';
import {gitCommandServiceSpy} from 'projects/git/src/lib/git-command/git-command.service.spec';
import {testGitStatus} from 'projects/git/src/lib/entities/git-status.spec';
import {TabsService} from 'projects/tabs/src/lib/tabs.service';

@Component({
  selector: 'lib-test',
  template: `test`
})
class TestComponent {
}

describe('GitStatusTabHeaderComponent', () => {
  let component: GitStatusTabHeaderComponent;
  let fixture: ComponentFixture<GitStatusTabHeaderComponent>;
  let gitCommandService: SpyObj<GitCommandService>;


  beforeEach(async(() => {
    gitCommandService = gitCommandServiceSpy();
    TestBed.configureTestingModule({
      declarations: [ GitStatusTabHeaderComponent ],
      providers: [
        TabsService,
        {provide: TAB_HEADER_DATA, useValue: newTestTab(TestComponent)},
        {provide: SIDE_HEADER_DATA, useValue: TabsSide.TOP},
        {provide: GitCommandService, useValue: gitCommandService},
      ]

    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(GitStatusTabHeaderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should set content', () => {
    gitCommandService.statusSubject.next(testGitStatus());
    expect(component.icon.content).toBe('5');
  });
});
