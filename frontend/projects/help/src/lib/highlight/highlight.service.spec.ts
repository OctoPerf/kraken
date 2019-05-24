import {fakeAsync, inject, TestBed, tick} from '@angular/core/testing';

import {Overlay} from '@angular/cdk/overlay';
import {HighlightModule} from 'projects/help/src/lib/highlight/highlight.module';
import {HighlightService} from 'projects/help/src/lib/highlight/highlight.service';

export const highlightServiceSpy = () => jasmine.createSpyObj('HighlightService', ['highlight']);

describe('HighlightService', () => {

  let overlay;
  let overlayRef;
  let document;
  let element;
  const rect = {top: 0, left: 0, right: 0, bottom: 0, width: 0, height: 0};

  beforeEach(() => {
    overlay = jasmine.createSpyObj('overlay', ['create']);
    overlayRef = jasmine.createSpyObj('overlayRef', ['attach', 'detach']);
    document = jasmine.createSpyObj('document', ['querySelector']);
    element = jasmine.createSpyObj('element', ['getBoundingClientRect']);
    overlay.create.and.returnValue(overlayRef);
    document.querySelector.and.returnValue(element);
    element.getBoundingClientRect.and.returnValue(rect);
    TestBed.configureTestingModule({
      imports: [HighlightModule],
      providers: [
        {provide: Overlay, useValue: overlay}
      ]
    });
  });

  it('should be created', inject([HighlightService], (service: HighlightService) => {
    expect(service).toBeTruthy();
    expect(service.overlayRef).toBe(overlayRef);
  }));

  it('should highlight default duration', fakeAsync(() => {
    const highlightService = TestBed.get(HighlightService);
    highlightService.document = document;
    highlightService.highlight('someShit');
    expect(overlayRef.attach).toHaveBeenCalled();
    expect(overlayRef.detach).not.toHaveBeenCalled();
    tick(801);
    expect(overlayRef.detach).toHaveBeenCalled();
  }));

  it('should highlight custom duration', fakeAsync(() => {
    const highlightService = TestBed.get(HighlightService);
    highlightService.document = document;
    highlightService.highlight('someShit', 200);
    expect(overlayRef.attach).toHaveBeenCalled();
    expect(overlayRef.detach).not.toHaveBeenCalled();
    tick(201);
    expect(overlayRef.detach).not.toHaveBeenCalled();
    tick(100); // min 300 ms
    expect(overlayRef.detach).toHaveBeenCalled();
  }));

  it('should not highlight', () => {
    const highlightService = TestBed.get(HighlightService);
    highlightService.document = document;
    document.querySelector.and.returnValue(null);
    highlightService.highlight('someShit', 200);
    expect(overlayRef.attach).not.toHaveBeenCalled();
  });
});
