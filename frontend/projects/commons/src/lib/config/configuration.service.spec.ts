import {TestBed} from '@angular/core/testing';

import {ConfigurationService, loadConfiguration} from './configuration.service';
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';
import {HttpClient} from '@angular/common/http';
import {fail} from 'assert';
import {Configuration} from './configuration';
import {ENVIRONMENT} from 'projects/commons/src/lib/config/configuration-environment';
import SpyObj = jasmine.SpyObj;

export const configurationServiceMock = (): ConfigurationService => {
  return new ConfigurationService(null, null);
};

export const configurationServiceSpy = (): SpyObj<ConfigurationService> => {
  const spy = jasmine.createSpyObj('ConfigurationService', ['url', 'value', 'projectApiUrl']);
  spy.projectApiUrl.and.callFake((path = '') => `projectApiUrl/project${path}`);
  return spy;
};

describe('ConfigurationService', () => {
  let httpClient: HttpClient;
  let httpTestingController: HttpTestingController;
  let configuration: ConfigurationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [
        ConfigurationService,
        {
          provide: ENVIRONMENT, // you can also use InjectionToken
          useValue: {configUrl: 'assets/config.json'}
        },
      ]
    });

    httpClient = TestBed.inject(HttpClient);
    httpTestingController = TestBed.inject(HttpTestingController);
    configuration = TestBed.inject(ConfigurationService);
  });

  it('should be created', () => {
    expect(configuration).toBeTruthy();
  });

  it('should load config', () => {
    const testConf: Configuration = {
      version: 'version',
      applicationId: 'application',
      backendApiUrl: 'backendApiUrl',
      docUrl: 'docUrl'
    };
    configuration.load().then(config => expect(config).toBeDefined(), () => fail('load failed'));
    const req = httpTestingController.expectOne('assets/config.json');
    expect(req.request.method).toEqual('GET');
    req.flush(testConf);
    expect(configuration._config.value).toEqual(testConf);
  });

  it('should loadConfiguration hook', () => {
    const configService = configurationServiceMock();
    spyOn(configService, 'load');
    loadConfiguration(configService)();
    expect(configService.load).toHaveBeenCalled();
  });

  it('should return applicationId', () => {
    expect(configuration.applicationId).toBe('application');
  });

  it('should return value', () => {
    expect(configuration.value('docUrl')).toBe('docUrl');
  });

  it('should return backend API URL', () => {
    expect(configuration.backendApiUrl).toBe('backendApiUrl');
  });

  it('should return Doc URL', () => {
    expect(configuration.docUrl('/path')).toBe('docUrl/path');
  });

  it('should return version', () => {
    expect(configuration.version('key')).toBe('version_key');
  });

  it('should return projectApiUrl', () => {
    expect(configuration.projectApiUrl('/path')).toBe('backendApiUrl/project/path');
  });

  it('should return projectApiUrl no param', () => {
    expect(configuration.projectApiUrl()).toBe('backendApiUrl/project');
  });

  it('should set/get projectId', () => {
    configuration.projectId = 'id';
    expect(configuration.projectId).toBe('id');
  });
});
