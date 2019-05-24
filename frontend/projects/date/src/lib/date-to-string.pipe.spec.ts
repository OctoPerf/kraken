import {DateToStringPipe} from 'projects/date/src/lib/date-to-string.pipe';

describe('DateToStringPipe', () => {
  it('create an instance', () => {
    const pipe = new DateToStringPipe();
    expect(pipe).toBeTruthy();
  });

  it('format date', () => {
    const pipe = new DateToStringPipe();
    expect(pipe.transform(0)).toEqual('1970-01-01');
  });
});
