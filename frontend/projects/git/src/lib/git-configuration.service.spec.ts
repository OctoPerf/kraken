import {TestBed} from '@angular/core/testing';

import {GitConfigurationService} from './git-configuration.service';
import {ConfigurationService} from 'projects/commons/src/lib/config/configuration.service';
import {configurationServiceSpy} from 'projects/commons/src/lib/config/configuration.service.spec';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import SpyObj = jasmine.SpyObj;

export const gitConfigurationServiceSpy = () => {
  const spy = jasmine.createSpyObj('GitConfigurationService', [
    'commandApiUrl',
    'projectApiUrl',
    'userApiUrl',
  ]);
  spy.commandApiUrl.and.callFake((path = '') => `commandApiUrl/command${path}`);
  spy.projectApiUrl.and.callFake((path = '') => `projectApiUrl/project${path}`);
  spy.userApiUrl.and.callFake((path = '') => `userApiUrl/user${path}`);
  return spy;
};

describe('GitConfigurationService', () => {
  let service: GitConfigurationService;
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
    service = TestBed.inject(GitConfigurationService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should return commandApiUrl', () => {
    configuration.url.and.returnValue('url');
    expect(service.commandApiUrl('/path')).toBe('url');
    expect(configuration.url).toHaveBeenCalledWith('backendApiUrl', '/git/command/path');
  });

  it('should return commandApiUrl no param', () => {
    configuration.url.and.returnValue('url');
    expect(service.commandApiUrl()).toBe('url');
    expect(configuration.url).toHaveBeenCalledWith('backendApiUrl', '/git/command');
  });

  it('should return projectApiUrl', () => {
    configuration.url.and.returnValue('url');
    expect(service.projectApiUrl('/path')).toBe('url');
    expect(configuration.url).toHaveBeenCalledWith('backendApiUrl', '/git/project/path');
  });

  it('should return projectApiUrl no param', () => {
    configuration.url.and.returnValue('url');
    expect(service.projectApiUrl()).toBe('url');
    expect(configuration.url).toHaveBeenCalledWith('backendApiUrl', '/git/project');
  });

  it('should return userApiUrl', () => {
    configuration.url.and.returnValue('url');
    expect(service.userApiUrl('/path')).toBe('url');
    expect(configuration.url).toHaveBeenCalledWith('backendApiUrl', '/git/user/path');
  });

  it('should return userApiUrl no param', () => {
    configuration.url.and.returnValue('url');
    expect(service.userApiUrl()).toBe('url');
    expect(configuration.url).toHaveBeenCalledWith('backendApiUrl', '/git/user');
  });

});
