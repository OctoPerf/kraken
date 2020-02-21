import {TestBed} from '@angular/core/testing';

import {StorageConfigurationService} from 'projects/storage/src/lib/storage-configuration.service';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {ConfigurationService} from 'projects/commons/src/lib/config/configuration.service';
import SpyObj = jasmine.SpyObj;
import {testStorageFileNode} from 'projects/storage/src/lib/entities/storage-node.spec';
import {configurationServiceSpy} from 'projects/commons/src/lib/config/configuration.service.spec';

export const storageConfigurationServiceSpy = () => {
  const spy = jasmine.createSpyObj('StorageConfigurationService', [
    'storageApiUrl',
  ]);
  spy.storageApiUrl.and.callFake((path = '') => `storageApiUrl/files${path}`);
  spy.readmeNode = {
    'path': 'gatling/README.md',
    'type': 'FILE',
    'depth': 1,
    'length': 0,
    'lastModified': 0
  };
  return spy;
};

describe('StorageConfigurationService', () => {
  let service: StorageConfigurationService;
  let configuration: SpyObj<ConfigurationService>;

  beforeEach(() => {
    configuration = configurationServiceSpy();
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [
        StorageConfigurationService,
        {
          provide: ConfigurationService,
          useValue: configuration,
        },
      ]
    });
    service = TestBed.inject(StorageConfigurationService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should return staticApiUrl', () => {
    configuration.url.and.returnValue('url');
    expect(service.storageApiUrl('path')).toBe('url');
  });

  it('should return readme node', () => {
    configuration.value.and.returnValue(testStorageFileNode());
    expect(service.readmeNode).toEqual(testStorageFileNode());
  });

});
