import {async, ComponentFixture, TestBed} from '@angular/core/testing';
import {AppComponent} from './app.component';
import {ProjectService} from 'projects/project/src/app/project/project.service';
import {projectServiceSpy} from 'projects/project/src/app/project/project.service.spec';
import SpyObj = jasmine.SpyObj;

describe('AppComponent', () => {
  let component: AppComponent;
  let fixture: ComponentFixture<AppComponent>;
  let projectService: SpyObj<ProjectService>;

  beforeEach(async(() => {
    projectService = projectServiceSpy();
    TestBed.configureTestingModule({
      declarations: [AppComponent],
      providers: [
        {provide: ProjectService, useValue: projectService},
      ]
    })
      .overrideTemplate(AppComponent, '')
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AppComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

});
