import {TestBed, inject} from '@angular/core/testing';
import {FormatterService} from 'projects/editor/src/lib/formatter.service';

describe('FormatterService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [FormatterService]
    });
  });

  it('should be created', inject([FormatterService], (service: FormatterService) => {
    expect(service).toBeTruthy();
  }));

  it('formatJSON() should format', inject([FormatterService], (service: FormatterService) => {
    expect(service.formatJSON('{}')).toBeDefined();
  }));

  it('formatXML() should format', inject([FormatterService], (service: FormatterService) => {
    expect(service.formatXML('<xml></xml>')).toBeDefined();
  }));

  it('formatHTML() should format', inject([FormatterService], (service: FormatterService) => {
    expect(service.formatHTML('<html></html>')).toBeDefined();
  }));

  it('formatJS() should format', inject([FormatterService], (service: FormatterService) => {
    expect(service.formatJS('function(){}')).toBeDefined();
  }));

  it('formatCSS() should format', inject([FormatterService], (service: FormatterService) => {
    expect(service.formatCSS('.CodeMirror { border: 1px solid @border-color; font-size: @font-size-xs;tgfvc  nhbgfdcx bnhj gfcdx}'))
      .toBeDefined();
  }));

});
