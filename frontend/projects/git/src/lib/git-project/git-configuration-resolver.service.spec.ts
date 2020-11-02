import {TestBed} from '@angular/core/testing';
import {of} from 'rxjs';
import {GitUserService} from 'projects/git/src/lib/git-user/git-user.service';
import {GitConfigurationResolverService} from 'projects/git/src/lib/git-project/git-configuration-resolver.service';
import {GitProjectService} from 'projects/git/src/lib/git-project/git-project.service';
import {gitProjectServiceSpy} from 'projects/git/src/lib/git-project/git-project.service.spec';
import {testGitConfiguration} from 'projects/git/src/lib/entities/git-configuration.spec';
import SpyObj = jasmine.SpyObj;

describe('GitConfigurationResolverService', () => {
  let service: GitConfigurationResolverService;
  let gitProjectService: SpyObj<GitProjectService>;

  beforeEach(() => {
    gitProjectService = gitProjectServiceSpy();
    TestBed.configureTestingModule({
      providers: [
        {provide: GitProjectService, useValue: gitProjectService}
      ]
    });
    service = TestBed.inject(GitConfigurationResolverService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should resolve', () => {
    const config = of(testGitConfiguration());
    gitProjectService.configuration.and.returnValue(config);
    expect(service.resolve(null, null)).toBe(config);
  });
});


