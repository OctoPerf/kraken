import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {ProjectNameInputComponent} from './project-name-input.component';
import {VendorsModule} from 'projects/vendors/src/lib/vendors.module';
import {FormControl, FormGroup} from '@angular/forms';

export const projectNameInputComponentSpy = () => {
  const spy = jasmine.createSpyObj('ProjectNameInputComponent', [
    '',
  ]);
  spy.projectName = new FormControl('', []);
  return spy;
};

describe('ProjectNameInputComponent', () => {
  let component: ProjectNameInputComponent;
  let fixture: ComponentFixture<ProjectNameInputComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ProjectNameInputComponent],
      imports: [VendorsModule],
      providers: []
    })
      .overrideTemplate(ProjectNameInputComponent, '')
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProjectNameInputComponent);
    component = fixture.componentInstance;
    component.projectForm = new FormGroup({});
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should return projectName', () => {
    expect(component.projectName.value).toBe('');
  });

});
