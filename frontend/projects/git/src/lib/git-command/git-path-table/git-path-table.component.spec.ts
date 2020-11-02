import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { GitPathTableComponent } from 'projects/git/src/lib/git-command/git-path-table/git-path-table.component';
import {testRenamedCopiedStatus} from 'projects/git/src/lib/entities/git-renamed-copied-status.spec';

describe('GitFileStatusTableComponent', () => {
  let component: GitPathTableComponent;
  let fixture: ComponentFixture<GitPathTableComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ GitPathTableComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(GitPathTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should set status', () => {
    component.paths = ['p1', 'p2'];
    expect(component.dataSource.data.length).toBe(2);
  });
});
