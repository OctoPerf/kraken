import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NotConnectedToGitComponent } from 'projects/git/src/lib/git-command/not-connected-to-git/not-connected-to-git.component';

describe('NotConnectedToGitComponent', () => {
  let component: NotConnectedToGitComponent;
  let fixture: ComponentFixture<NotConnectedToGitComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NotConnectedToGitComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NotConnectedToGitComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
