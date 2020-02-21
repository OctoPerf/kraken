import {fakeAsync, TestBed, tick} from '@angular/core/testing';

import {GuiToolsService} from './gui-tools.service';

export const guiToolsServiceSpy = () => {
  const spy = jasmine.createSpyObj('GuiToolsService', [
    'scrollTo',
    'copyToClipboard',
  ]);
  return spy;
};

describe('GuiToolsService', () => {


  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: GuiToolsService = TestBed.inject(GuiToolsService);
    expect(service).toBeTruthy();
  });

  it('should not scroll', fakeAsync(() => {
    const getElement: any = () => null;
    const scrollableElement: any = null;
    const service: GuiToolsService = TestBed.inject(GuiToolsService);
    service.scrollTo(scrollableElement, getElement);
    tick(250);
  }));

  it('should scroll up', fakeAsync(() => {
    const getElement: any = () => {
      return {
        getBoundingClientRect: () => {
          return {top: 1, bottom: 10};
        }
      };
    };
    const scrollableElement: any = {
      nativeElement: {
        offsetHeight: 100,
        scrollTop: 10,
        getBoundingClientRect: () => {
          return {top: 50};
        }
      }
    };

    const service: GuiToolsService = TestBed.inject(GuiToolsService);
    service.scrollTo(scrollableElement, getElement);
    tick(250);
    expect(scrollableElement.nativeElement.scrollTop).toBe(-89);
  }));

  it('should scroll down', fakeAsync(() => {
    const getElement: any = () => {
      return {
        getBoundingClientRect: () => {
          return {top: 200, bottom: 10};
        }
      };
    };
    const scrollableElement: any = {
      nativeElement: {
        offsetHeight: 100,
        scrollTop: 10,
        getBoundingClientRect: () => {
          return {top: 50};
        }
      }
    };

    const service: GuiToolsService = TestBed.inject(GuiToolsService);
    service.scrollTo(scrollableElement, getElement);
    tick(250);
    expect(scrollableElement.nativeElement.scrollTop).toBe(10);
  }));
});
