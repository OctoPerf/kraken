import {ComponentFixture, TestBed, waitForAsync} from '@angular/core/testing';

import {GitRenamedCopiedStatusTableComponent} from 'projects/git/src/lib/git-command/git-renamed-copied-status-table/git-renamed-copied-status-table.component';
import {testRenamedCopiedStatus} from 'projects/git/src/lib/entities/git-renamed-copied-status.spec';

describe('GitFileStatusTableComponent', () => {
  let component: GitRenamedCopiedStatusTableComponent;
  let fixture: ComponentFixture<GitRenamedCopiedStatusTableComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [ GitRenamedCopiedStatusTableComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(GitRenamedCopiedStatusTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should set status', () => {
    component.renamedCopiedStatuses = [testRenamedCopiedStatus(), testRenamedCopiedStatus()];
    expect(component.dataSource.data.length).toBe(2);
  });
});
