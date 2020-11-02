import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {GitFileStatusTableComponent} from './git-file-status-table.component';
import {testGitFileStatus} from 'projects/git/src/lib/entities/git-file-status.spec';

describe('GitFileStatusTableComponent', () => {
  let component: GitFileStatusTableComponent;
  let fixture: ComponentFixture<GitFileStatusTableComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [GitFileStatusTableComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(GitFileStatusTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should set status', () => {
    component.fileStatuses = [testGitFileStatus(), testGitFileStatus()];
    expect(component.dataSource.data.length).toBe(2);
  });
});
