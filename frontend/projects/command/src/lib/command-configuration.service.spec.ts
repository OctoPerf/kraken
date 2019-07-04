import {TestBed} from '@angular/core/testing';

import {StorageConfigurationService} from 'projects/storage/src/lib/storage-configuration.service';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {ConfigurationService} from 'projects/commons/src/lib/config/configuration.service';
import SpyObj = jasmine.SpyObj;
import {CommandConfigurationService} from 'projects/command/src/lib/command-configuration.service';
import {configurationServiceSpy} from 'projects/commons/src/lib/config/configuration.service.spec';

export const commandConfigurationServiceSpy = () => {
  const spy = jasmine.createSpyObj('CommandConfigurationService', [
    'commandApiUrl',
  ]);
  spy.commandApiUrl.and.callFake((path = '') => `commandApiUrl/command${path}`);
  return spy;
};

describe('CommandConfigurationService', () => {
  let service: CommandConfigurationService;
  let configuration: SpyObj<ConfigurationService>;

  beforeEach(() => {
    configuration = configurationServiceSpy();
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [
        CommandConfigurationService,
        {
          provide: ConfigurationService,
          useValue: configuration,
        },
      ]
    });
    service = TestBed.get(CommandConfigurationService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should return staticApiUrl', () => {
    configuration.url.and.returnValue('url');
    expect(service.commandApiUrl('path')).toBe('url');
  });

});
