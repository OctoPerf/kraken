import {TestBed, inject} from '@angular/core/testing';
import {StringToolsService} from './string-tools.service';

describe('StringToolsService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [StringToolsService]
    });
  });

  it('should be created', inject([StringToolsService], (service: StringToolsService) => {
    expect(service).toBeTruthy();
  }));

  it('should convert a number to a-z character', inject([StringToolsService], (service: StringToolsService) => {
    expect(service.numberToAlphabet(0)).toBe('a');
    expect(service.numberToAlphabet(25)).toBe('z');
    expect(service.numberToAlphabet(26)).toBe('a');
  }));

  it('should replaceAll', inject([StringToolsService], (service: StringToolsService) => {
    expect(service.replaceAll('abaabababa', 'ab', 'c')).toBe('caccca');
    expect(service.numberToAlphabet(25)).toBe('z');
    expect(service.numberToAlphabet(26)).toBe('a');
  }));

  it('should trunc start string', inject([StringToolsService], (service: StringToolsService) => {
    expect(service.truncStartString('0123456789', 10)).toBe('0123456789');
    expect(service.truncStartString('0123456789', 8)).toBe('0123456789');
    expect(service.truncStartString('0123456789', 7)).toBe('0123456789');
    expect(service.truncStartString('0123456789', 6)).toBe('...456789');
    expect(service.truncStartString('0123456789', 3)).toBe('...789');
  }));

  it('should trunc end string', inject([StringToolsService], (service: StringToolsService) => {
    expect(service.truncEndString('0123456789', 10)).toBe('0123456789');
    expect(service.truncEndString('0123456789', 6)).toBe('012...');
    expect(service.truncEndString('0123456789', 3)).toBe('...');
  }));

  it('should escape and unescape regexp', inject([StringToolsService], (service: StringToolsService) => {
    const str = '[l\\od[ash]($.https://lo?dash{.com/)^*}+';
    const escaped = service.escapeRegExp(str);
    const unescaped = service.unescapeRegExp(escaped);
    expect(unescaped).toBe(str);
  }));

  it('should unescape empty', inject([StringToolsService], (service: StringToolsService) => {
    expect(service.unescapeRegExp(null)).toBeNull();
  }));

  it('should escape regexp', inject([StringToolsService], (service: StringToolsService) => {
    expect(service.escapeRegExp(null)).toBe('');
    expect(service.escapeRegExp(undefined)).toBe('');
  }));

});
