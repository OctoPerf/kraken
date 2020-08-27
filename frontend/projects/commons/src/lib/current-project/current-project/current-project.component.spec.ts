import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {CurrentProjectComponent} from './current-project.component';
import {CurrentProjectService} from 'projects/commons/src/lib/current-project/current-project.service';
import {currentProjectServiceSpy} from 'projects/commons/src/lib/current-project/current-project.service.spec';

describe('CurrentProjectComponent', () => {
  let component: CurrentProjectComponent;
  let fixture: ComponentFixture<CurrentProjectComponent>;

  beforeEach(async(() => {
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
