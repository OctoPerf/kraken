import {TestBed} from '@angular/core/testing';

import {GatlingConfigurationService} from './gatling-configuration.service';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {ConfigurationService} from 'projects/commons/src/lib/config/configuration.service';
import SpyObj = jasmine.SpyObj;
import {testStorageRootNode} from 'projects/storage/src/lib/entities/storage-node.spec';
import {configurationServiceSpy} from 'projects/commons/src/lib/config/configuration.service.spec';

export const gatlingConfigurationServiceSpy = () => {
  const spy = jasmine.createSpyObj('GatlingConfigurationService', [
    'nope',
  ]);
  spy.simulationsRootNode = {
    path: 'gatling/user-files/simulations',
    type: 'DIRECTORY',
    depth: 2,
    length: 0,
    lastModified: 0,
  };
  spy.resourcesRootNode = {
    path: 'gatling/user-files/resources',
    type: 'DIRECTORY',
    depth: 2,
    length: 0,
    lastModified: 0,
  };
  return spy;
};

describe('GatlingConfigurationService', () => {
  let service: GatlingConfigurationService;
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
    service = TestBed.inject(GatlingConfigurationService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should return simulationsRootNode', () => {
    const node = testStorageRootNode();
    configuration.value.and.returnValue(node);
    expect(service.simulationsRootNode).toBe(node);
  });

  it('should return resourcesRootNode', () => {
    const node = testStorageRootNode();
    configuration.value.and.returnValue(node);
    expect(service.resourcesRootNode).toBe(node);
  });

});
