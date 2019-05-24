import {DateTimeToStringPipe} from './date-time-to-string.pipe';

describe('DateTimeToStringPipe', () => {
  it('create an instance', () => {
    const pipe = new DateTimeToStringPipe();
    expect(pipe).toBeTruthy();
  });

  it('format date', () => {
    const pipe = new DateTimeToStringPipe();
    expect(pipe.transform(0)).toEqual('1970-01-01 01:00:00');
  });
});
