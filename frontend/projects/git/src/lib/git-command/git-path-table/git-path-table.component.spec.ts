import {ComponentFixture, TestBed, waitForAsync} from '@angular/core/testing';

import {GitPathTableComponent} from 'projects/git/src/lib/git-command/git-path-table/git-path-table.component';

describe('GitFileStatusTableComponent', () => {
  let component: GitPathTableComponent;
  let fixture: ComponentFixture<GitPathTableComponent>;

  beforeEach(waitForAsync(() => {
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
