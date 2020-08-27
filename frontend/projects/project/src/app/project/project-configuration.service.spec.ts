import {TestBed} from '@angular/core/testing';

import {ProjectConfigurationService} from 'projects/project/src/app/project/project-configuration.service';
import {ConfigurationService} from 'projects/commons/src/lib/config/configuration.service';
import {configurationServiceSpy} from 'projects/commons/src/lib/config/configuration.service.spec';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import SpyObj = jasmine.SpyObj;

export const projectConfigurationServiceSpy = () => {
  const spy = jasmine.createSpyObj('ProjectConfigurationService', [
    'availableApplications',
  ]);
  spy.availableApplications.and.returnValue(['gatling']);
  return spy;
};

describe('ProjectConfigurationService', () => {
  let service: ProjectConfigurationService;
  let configuration: SpyObj<ConfigurationService>;

  beforeEach(() => {
    configuration = configurationServiceSpy();
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [
        ConfigurationService,
        {
          provide: ConfigurationService,
          useValue: configuration,
        },
      ]
    });
    service = TestBed.inject(ProjectConfigurationService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should return available applications', () => {
    configuration.value.and.returnValue(['gatling']);
    expect(service.availableApplications()).toEqual(['gatling']);
  });
});
