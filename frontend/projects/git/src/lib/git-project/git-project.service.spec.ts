import {TestBed} from '@angular/core/testing';

import {GitProjectService} from './git-project.service';
import {BehaviorSubject} from 'rxjs';
import {testGitConfiguration} from 'projects/git/src/lib/entities/git-configuration.spec';
import {HttpTestingController} from '@angular/common/http/testing';
import {CoreTestModule} from 'projects/commons/src/lib/core/core.module.spec';
import {GitConfigurationService} from 'projects/git/src/lib/git-configuration.service';
import {gitConfigurationServiceSpy} from 'projects/git/src/lib/git-configuration.service.spec';

export const gitProjectServiceSpy = () => {
  const spy = jasmine.createSpyObj('GitProjectService', [
    'connect',
    'configuration',
    'disconnect',
    'isConnected',
  ]);
  spy.configurationSubject = new BehaviorSubject(testGitConfiguration());
  return spy;
};

describe('GitProjectService', () => {
  let service: GitProjectService;
  let httpTestingController: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [CoreTestModule],
      providers: [
        {provide: GitConfigurationService, useValue: gitConfigurationServiceSpy()},
        GitProjectService,
      ]
    });
    service = TestBed.inject(GitProjectService);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpTestingController.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should connect', () => {
    expect(service.isConnected()).toBeFalse();
    const config = testGitConfiguration();
    service.connect(config.repositoryUrl).subscribe(data => expect(data).toBe(config), () => fail('connect failed'));
    const request = httpTestingController.expectOne('projectApiUrl/project/connect?repositoryUrl=repositoryUrl');
    expect(request.request.method).toBe('POST');
    request.flush(config);
    expect(service.configurationSubject.getValue()).toBe(config);
    expect(service.isConnected()).toBeTrue();
  });

  it('should return configuration', () => {
    const config = testGitConfiguration();
    service.configuration().subscribe(data => expect(data).toBe(config), () => fail('connect failed'));
    const request = httpTestingController.expectOne('projectApiUrl/project/configuration');
    expect(request.request.method).toBe('GET');
    request.flush(config);
    expect(service.configurationSubject.getValue()).toBe(config);
  });

  it('should disconnect', () => {
    service.configurationSubject.next(testGitConfiguration());
    service.disconnect().subscribe();
    const request = httpTestingController.expectOne('projectApiUrl/project/disconnect');
    expect(request.request.method).toBe('DELETE');
    request.flush('');
    expect(service.configurationSubject.getValue()).toBeNull();
  });
});
