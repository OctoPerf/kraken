import {TestBed} from '@angular/core/testing';

import {AnalysisConfigurationService} from 'projects/analysis/src/lib/analysis-configuration.service';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {ConfigurationService} from 'projects/commons/src/lib/config/configuration.service';
import SpyObj = jasmine.SpyObj;
import {testStorageFileNode} from 'projects/storage/src/lib/entities/storage-node.spec';
import {configurationServiceSpy} from 'projects/commons/src/lib/config/configuration.service.spec';

export const analysisConfigurationServiceSpy = () => {
  const spy = jasmine.createSpyObj('AnalysisConfigurationService', [
    'analysisApiUrl',
    'staticApiUrl',
  ]);
  spy.staticApiUrl.and.callFake((path) => 'staticApiUrl' + path);
  spy.analysisApiUrl.and.callFake((path = '') => `analysisApiUrl/result${path}`);
  spy.analysisRootNode = {
    'path': 'gatling/results',
    'type': 'DIRECTORY',
    'depth': 1,
    'length': 0,
    'lastModified': 0
  };
  return spy;
};

describe('AnalysisConfigurationService', () => {
  let service: AnalysisConfigurationService;
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
    service = TestBed.inject(AnalysisConfigurationService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should return analysisApiUrl', () => {
    configuration.url.and.returnValue('url');
    expect(service.analysisApiUrl('path')).toBe('url');
  });

  it('should return analysisRootNode', () => {
    const node = testStorageFileNode();
    configuration.value.and.returnValue(node);
    expect(service.analysisRootNode).toEqual(node);
  });

  it('should return staticApiUrl', () => {
    configuration.url.and.returnValue('url');
    expect(service.staticApiUrl('path')).toBe('url');
  });
});
