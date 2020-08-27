import {TestBed} from '@angular/core/testing';
import {ProjectIdGuard} from 'projects/commons/src/lib/config/project-id.guard';
import {ConfigurationService} from 'projects/commons/src/lib/config/configuration.service';
import {configurationServiceMock} from 'projects/commons/src/lib/config/configuration.service.spec';
import {ActivatedRouteSnapshot} from '@angular/router';

describe('ProjectIdGuard', () => {
  let guard: ProjectIdGuard;
  let configuration: ConfigurationService;

  beforeEach(() => {
    configuration = configurationServiceMock();
    TestBed.configureTestingModule({
      providers: [
        {provide: ConfigurationService, useValue: configuration},
      ]
    });
    guard = TestBed.inject(ProjectIdGuard);
  });

  it('should be created', () => {
    expect(guard).toBeTruthy();
  });

  it('should canActivate block', () => {
    const next = {
      paramMap: new Map(),
    } as any;
    const response = guard.canActivate(next, null);
    expect(response).toBeFalse();
  });

  it('should canActivate continue', () => {
    const next = {
      paramMap: new Map(),
    } as any;
    next.paramMap.set('projectId', 'projectId');
    const response = guard.canActivate(next, null);
    expect(response).toBeTrue();
    expect(configuration.projectId).toBe('projectId');
  });
});

