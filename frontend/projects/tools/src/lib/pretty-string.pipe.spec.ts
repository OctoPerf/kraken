import {PrettyStringPipe} from './pretty-string.pipe';
import {StringToolsService} from './string-tools.service';
import {TestBed} from '@angular/core/testing';

describe('PrettyStringPipe', () => {
  let pipe: PrettyStringPipe;

  beforeEach(() => {
    TestBed.configureTestingModule({declarations: [PrettyStringPipe], providers: [PrettyStringPipe, StringToolsService]});
    pipe = TestBed.get(PrettyStringPipe);
  });

  it('create an instance', () => {
    expect(pipe.transform('MY_UGLY_STRING')).toBe('My ugly string');
  });
});
