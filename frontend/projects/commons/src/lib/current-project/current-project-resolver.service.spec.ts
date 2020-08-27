import {TestBed} from '@angular/core/testing';

import {CurrentProjectResolverService} from 'projects/commons/src/lib/current-project/current-project-resolver.service';
import {of} from 'rxjs';
import {testProject} from 'projects/project/src/app/project/entities/project.spec';
import {CurrentProjectService} from 'projects/commons/src/lib/current-project/current-project.service';
import SpyObj = jasmine.SpyObj;
import {currentProjectServiceSpy} from 'projects/commons/src/lib/current-project/current-project.service.spec';

describe('CurrentProjectResolverService', () => {
  let service: CurrentProjectResolverService;
  let currentProjectService: SpyObj<CurrentProjectService>;

  beforeEach(() => {
    currentProjectService = currentProjectServiceSpy();
    TestBed.configureTestingModule({
      providers: [
        {provide: CurrentProjectService, useValue: currentProjectService}
      ]
    });
    service = TestBed.inject(CurrentProjectResolverService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should resolve', () => {
    const project = of(testProject());
    currentProjectService.getProject.and.returnValue(project);
    expect(service.resolve(null, null)).toBe(project);
  });
});


