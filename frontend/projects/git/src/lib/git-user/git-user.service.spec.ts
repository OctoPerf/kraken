import {TestBed} from '@angular/core/testing';

import {GitUserService} from './git-user.service';
import {HttpTestingController} from '@angular/common/http/testing';
import {CoreTestModule} from 'projects/commons/src/lib/core/core.module.spec';
import {GitConfigurationService} from 'projects/git/src/lib/git-configuration.service';
import {gitConfigurationServiceSpy} from 'projects/git/src/lib/git-configuration.service.spec';
import {BehaviorSubject} from 'rxjs';

export const gitUserServiceSpy = () => {
  const spy = jasmine.createSpyObj('GitUserService', [
    'publicKey',
  ]);
  spy.publicKeySubject = new BehaviorSubject('');
  return spy;
};

describe('GitUserService', () => {
  let service: GitUserService;
  let httpTestingController: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [CoreTestModule],
      providers: [
        {provide: GitConfigurationService, useValue: gitConfigurationServiceSpy()},
        GitUserService,
      ]
    });
    service = TestBed.inject(GitUserService);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpTestingController.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should return public key', () => {
    const publicKey = 'test';
    service.publicKey().subscribe(data => expect(data).toBe(publicKey), () => fail('publicKey failed'));
    const request = httpTestingController.expectOne('userApiUrl/user/publicKey');
    expect(request.request.method).toBe('GET');
    request.flush(publicKey);
  });

});
