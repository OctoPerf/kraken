import {TestBed, inject, fakeAsync, tick} from '@angular/core/testing';

import {WindowService} from './window.service';
import {of, throwError} from 'rxjs';

export const windowSpy = () => jasmine.createSpyObj('WindowService', ['resize', 'resizeNow', 'open']);

describe('WindowService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [WindowService]
    });
  });

  it('should be created', inject([WindowService], (service: WindowService) => {
    expect(service).toBeTruthy();
  }));

  it('should open', fakeAsync(() => {
    const service = new WindowService();
    const tab: any = {
      location: {
        href: ''
      }
    };
    const open = spyOn(service.window, 'open');
    open.and.returnValue(tab);
    service.open(of('url'));
    expect(open).toHaveBeenCalled();
    expect(tab.location.href).toBe('url');
  }));

  it('should open fail', fakeAsync(() => {
    const service = new WindowService();
    const tab: any = {
      close: jasmine.createSpy('close')
    };
    const open = spyOn(service.window, 'open');
    open.and.returnValue(tab);
    service.open(throwError('fail'));
    expect(open).toHaveBeenCalled();
    expect(tab.close).toHaveBeenCalled();
  }));

  it('should resize', fakeAsync(() => {
    const service = new WindowService();
    const dispatch = spyOn(service.window, 'dispatchEvent');
    dispatch.and.callThrough();
    service.resize();
    tick(51);
    expect(dispatch).toHaveBeenCalledWith(new Event('resize'));
  }));

  it('should resize now', fakeAsync(() => {
    const service = new WindowService();
    const dispatch = spyOn(service.window, 'dispatchEvent');
    dispatch.and.callThrough();
    service.resizeNow();
    tick(1);
    expect(dispatch).toHaveBeenCalledWith(new Event('resize'));
  }));
});
