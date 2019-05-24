import {inject, TestBed} from '@angular/core/testing';

import {LocalStorageService} from './local-storage.service';
import {CoreTestModule} from 'projects/commons/src/lib/core/core.module.spec';
import {ConfigurationService} from 'projects/commons/src/lib/config/configuration.service';
import {configurationServiceMock} from 'projects/commons/src/lib/config/configuration.service.spec';

export const localStorageServiceSpy = () => {
  const spy = jasmine.createSpyObj('LocalStorageService', ['set',
    'getString',
    'getNumber',
    'getBoolean',
    'setItem',
    'getItem',
    'clear',
    'remove']);
  spy.getString.and.callFake((key: string, defaultValue?: any) => {
    return defaultValue;
  });
  spy.getNumber.and.callFake((key: string, defaultValue?: any) => {
    return defaultValue;
  });
  spy.getBoolean.and.callFake((key: string, defaultValue?: any) => {
    return defaultValue;
  });
  spy.getItem.and.callFake((key: string, defaultValue?: any) => {
    return defaultValue;
  });
  return spy;
};

describe('LocalStorageService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [CoreTestModule],
      providers: [
        LocalStorageService,
        {provide: ConfigurationService, useValue: configurationServiceMock()}
      ]
    });
  });

  it('should be created', inject([LocalStorageService], (service: LocalStorageService) => {
    expect(service).toBeTruthy();
    service.clear();
  }));

  it('should set and get string', inject([LocalStorageService], (service: LocalStorageService) => {
    const key = 'strKey';
    const value = 'strValue';
    service.remove(key);
    service.set(key, value);
    expect(service.getString(key)).toBe(value);
    service.remove(key);
    expect(service.getString(key)).toBeUndefined();
    expect(service.getString(key, 'val')).toBe('val');
  }));

  it('should set and get boolean', inject([LocalStorageService], (service: LocalStorageService) => {
    const key = 'boolKey';
    const value = false;
    service.remove(key);
    service.set(key, value);
    expect(service.getBoolean(key)).toBe(value);
  }));

  it('should set and get number', inject([LocalStorageService], (service: LocalStorageService) => {
    const key = 'numberKey';
    const value = 42;
    service.remove(key);
    service.set(key, value);
    expect(service.getNumber(key)).toBe(value);
  }));

  it('should set and get item', inject([LocalStorageService], (service: LocalStorageService) => {
    const key = 'strKey';
    const value = {value: 'strValue'};
    service.remove(key);
    service.setItem(key, value);
    expect(service.getItem(key)).toEqual(value);
    service.remove(key);
    expect(service.getItem(key)).toBeUndefined();
    expect(service.getItem(key, {value: 'default'})).toEqual({value: 'default'});
  }));
});
