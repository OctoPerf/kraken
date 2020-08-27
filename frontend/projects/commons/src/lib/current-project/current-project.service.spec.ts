import {TestBed} from '@angular/core/testing';

import {CurrentProjectService} from './current-project.service';
import {BehaviorSubject} from 'rxjs';
import {Project} from 'projects/project/src/app/project/entities/project';
import {RuntimeTaskService} from 'projects/runtime/src/lib/runtime-task/runtime-task.service';
import {HttpTestingController} from '@angular/common/http/testing';
import {EventBusService} from 'projects/event/src/lib/event-bus.service';
import {CoreTestModule} from 'projects/commons/src/lib/core/core.module.spec';
import {RuntimeConfigurationService} from 'projects/runtime/src/lib/runtime-configuration.service';
import {runtimeConfigurationServiceSpy} from 'projects/runtime/src/lib/runtime-configuration.service.spec';
import {testTasks} from 'projects/runtime/src/lib/entities/task.spec';
import {ConfigurationService} from 'projects/commons/src/lib/config/configuration.service';
import {configurationServiceSpy} from 'projects/commons/src/lib/config/configuration.service.spec';
import {testProject} from 'projects/project/src/app/project/entities/project.spec';

export const currentProjectServiceSpy = () => {
  const spy = jasmine.createSpyObj('CurrentProjectService', [
    'getProject',
  ]);
  spy.currentProject = new BehaviorSubject<Project>(null);
  return spy;
};

describe('CurrentProjectService', () => {
  let service: CurrentProjectService;
  let httpTestingController: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [CoreTestModule],
      providers: [
        {provide: ConfigurationService, useValue: configurationServiceSpy()},
        CurrentProjectService,
      ]
    });
    service = TestBed.inject(CurrentProjectService);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpTestingController.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should get', () => {
    const project = testProject();
    service.getProject().subscribe(data => expect(data).toBe(project), () => fail('get failed'));
    const request = httpTestingController.expectOne('projectApiUrl/project');
    expect(request.request.method).toBe('GET');
    request.flush(project);
    expect(service.currentProject.value).toBe(project);
  });

});
