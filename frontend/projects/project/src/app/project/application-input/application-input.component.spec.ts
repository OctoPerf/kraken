import {ComponentFixture, TestBed, waitForAsync} from '@angular/core/testing';

import {ApplicationInputComponent} from './application-input.component';
import {ProjectConfigurationService} from 'projects/project/src/app/project/project-configuration.service';
import {projectConfigurationServiceSpy} from 'projects/project/src/app/project/project-configuration.service.spec';
import {VendorsModule} from 'projects/vendors/src/lib/vendors.module';
import {FormControl, FormGroup} from '@angular/forms';
import SpyObj = jasmine.SpyObj;

export const applicationInputComponentSpy = () => {
  const spy = jasmine.createSpyObj('ApplicationInputComponent', [
    '',
  ]);
  spy.applicationId = new FormControl('', []);
  return spy;
};

describe('ApplicationInputComponent', () => {
  let component: ApplicationInputComponent;
  let fixture: ComponentFixture<ApplicationInputComponent>;
  let projectConfiguration: SpyObj<ProjectConfigurationService>;

  beforeEach(waitForAsync(() => {
    projectConfiguration = projectConfigurationServiceSpy();

    TestBed.configureTestingModule({
      declarations: [ApplicationInputComponent],
      imports: [VendorsModule],
      providers: [
        {provide: ProjectConfigurationService, useValue: projectConfiguration},
      ]
    })
      .overrideTemplate(ApplicationInputComponent, '')
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ApplicationInputComponent);
    component = fixture.componentInstance;
    component.projectForm = new FormGroup({});
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should return applicationId', () => {
    expect(component.applicationId.value).toBe('gatling');
  });

});
