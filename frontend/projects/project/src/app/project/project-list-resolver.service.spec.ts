import {TestBed} from '@angular/core/testing';

import {ProjectListResolverService} from './project-list-resolver.service';
import SpyObj = jasmine.SpyObj;
import {ProjectService} from 'projects/project/src/app/project/project.service';
import {projectServiceSpy} from 'projects/project/src/app/project/project.service.spec';
import {of} from 'rxjs';
import {testProjects} from 'projects/project/src/app/project/entities/project.spec';

describe('ProjectListResolverService', () => {
  let service: ProjectListResolverService;
  let projectService: SpyObj<ProjectService>;

  beforeEach(() => {
    projectService = projectServiceSpy();
    TestBed.configureTestingModule({
      providers: [
        {provide: ProjectService, useValue: projectService}
      ]
    });
    service = TestBed.inject(ProjectListResolverService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should resolve', () => {
    const projects = of(testProjects());
    projectService.listProjects.and.returnValue(projects);
    expect(service.resolve(null, null)).toBe(projects);
  });
});


