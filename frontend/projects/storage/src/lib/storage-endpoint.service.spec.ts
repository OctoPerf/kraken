import {TestBed} from '@angular/core/testing';

import {STORAGE_ENDPOINT_ROOT, StorageEndpointService} from './storage-endpoint.service';
import {ConfigurationService} from 'projects/commons/src/lib/config/configuration.service';
import {configurationServiceMock} from 'projects/commons/src/lib/config/configuration.service.spec';

export const storageEndpointServiceSpy = () => {
  const spy = jasmine.createSpyObj('StorageEndpointService', [
    'path',
  ]);
  spy.path.and.callFake((path: string) => 'storageApiUrl/files' + path);
  spy.root = '/files';
  return spy;
};

describe('StorageEndpointService', () => {
  let service: StorageEndpointService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        StorageEndpointService,
        {provide: STORAGE_ENDPOINT_ROOT, useValue: '/files'},
        {provide: ConfigurationService, useValue: configurationServiceMock()}
      ]
    });
    service = TestBed.get(StorageEndpointService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should return path', () => {
    expect(service.path('/suffix')).toBe('storageApiUrl/files/suffix');
  });

  it('should return root', () => {
    expect(service.root).toBe('/files');
  });
});
