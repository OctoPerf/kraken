import {ComponentFixture, TestBed, waitForAsync} from '@angular/core/testing';

import {CurrentProjectComponent} from 'projects/git/src/lib/git-project/current-project/current-project.component';
import {CurrentProjectService} from 'projects/git/src/lib/git-project/current-project/current-project.service';
import {currentProjectServiceSpy} from 'projects/git/src/lib/git-project/current-project/current-project.service.spec';

describe('CurrentProjectComponent', () => {
  let component: CurrentProjectComponent;
  let fixture: ComponentFixture<CurrentProjectComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [CurrentProjectComponent],
      providers: [
        {provide: CurrentProjectService, useValue: currentProjectServiceSpy()}
      ]
    })
      .overrideTemplate(CurrentProjectComponent, '')
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CurrentProjectComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
