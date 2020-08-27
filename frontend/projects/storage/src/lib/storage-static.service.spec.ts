import {TestBed} from '@angular/core/testing';

import {StorageStaticService} from './storage-static.service';
import {CoreTestModule} from 'projects/commons/src/lib/core/core.module.spec';
import {StorageConfigurationService} from 'projects/storage/src/lib/storage-configuration.service';
import {storageConfigurationServiceSpy} from 'projects/storage/src/lib/storage-configuration.service.spec';
import {cookiesServiceSpy} from 'projects/commons/src/lib/mock/cookies.mock.spec';
import {WindowService} from 'projects/tools/src/lib/window.service';
import {CookieService} from 'ngx-cookie-service';
import {SecurityService} from 'projects/security/src/lib/security.service';
import {windowServiceSpy} from 'projects/tools/src/lib/window.service.spec';
import {securityServiceSpy} from 'projects/security/src/lib/security.service.spec';
import {of} from 'rxjs';
import SpyObj = jasmine.SpyObj;
import {ConfigurationService} from 'projects/commons/src/lib/config/configuration.service';
import {
  configurationServiceMock,
  configurationServiceSpy
} from 'projects/commons/src/lib/config/configuration.service.spec';
import {testStorageDirectoryNode} from 'projects/storage/src/lib/entities/storage-node.spec';

export const storageStaticServiceSpy = () => {
  const spy = jasmine.createSpyObj('StorageStaticService', [
    'openStaticPage',
  ]);
  return spy;
};

describe('StorageStaticService', () => {
  let service: StorageStaticService;
  let window: SpyObj<WindowService>;
  let cookies: SpyObj<CookieService>;
  let securityService: SpyObj<SecurityService>;
  let storageConfiguration: SpyObj<StorageConfigurationService>;
  let configuration: ConfigurationService;

  beforeEach(() => {
    window = windowServiceSpy();
    cookies = cookiesServiceSpy();
    securityService = securityServiceSpy();
    storageConfiguration = storageConfigurationServiceSpy();
    configuration = configurationServiceMock();

    TestBed.configureTestingModule({
      imports: [CoreTestModule],
      providers: [
        {provide: WindowService, useValue: window},
        {provide: CookieService, useValue: cookies},
        {provide: SecurityService, useValue: securityService},
        {provide: StorageConfigurationService, useValue: storageConfiguration},
        {provide: ConfigurationService, useValue: configuration},
        StorageStaticService,
      ]
    });
    service = TestBed.inject(StorageStaticService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should open static page', () => {
    (securityService as any).token = of('token');
    window.open.and.callFake(url => url.subscribe((value) => expect(value).toEqual('backendApiUrl/files/static/path')));
    service.openStaticPage('path');
    expect(window.open).toHaveBeenCalled();
    expect(cookies.delete).toHaveBeenCalledWith('JWT');
    expect(cookies.set).toHaveBeenCalledWith('JWT', 'token', null, '/', null, false, 'Lax');
  });

  it('should open download link', () => {
    const node = testStorageDirectoryNode();
    (securityService as any).token = of('token');
    window.open.and.callFake(url => url.subscribe((value) => expect(value).toEqual('backendApiUrl/files/static/' + node.path)));
    service.openDownloadLink(node);
    expect(window.open).toHaveBeenCalled();
    expect(cookies.delete).toHaveBeenCalledWith('JWT');
    expect(cookies.set).toHaveBeenCalledWith('JWT', 'token', null, '/', null, false, 'Lax');
    expect(storageConfiguration.storageApiUrl).toHaveBeenCalledWith('/static/' + node.path);
  });

  it('should open download link no node', () => {
    (securityService as any).token = of('token');
    window.open.and.callFake(url => url.subscribe((value) => expect(value).toEqual('backendApiUrl/files/static/')));
    service.openDownloadLink();
    expect(window.open).toHaveBeenCalled();
    expect(cookies.delete).toHaveBeenCalledWith('JWT');
    expect(cookies.set).toHaveBeenCalledWith('JWT', 'token', null, '/', null, false, 'Lax');
    expect(storageConfiguration.storageApiUrl).toHaveBeenCalledWith('/static/');
  });

});
