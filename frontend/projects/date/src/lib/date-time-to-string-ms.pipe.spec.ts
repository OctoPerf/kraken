import { DateTimeToStringMsPipe } from './date-time-to-string-ms.pipe';

describe('DateTimeToStringMsPipe', () => {
  it('create an instance', () => {
    const pipe = new DateTimeToStringMsPipe();
    expect(pipe).toBeTruthy();
  });

  it('format date', () => {
    const pipe = new DateTimeToStringMsPipe();
    expect(pipe.transform(0)).toEqual('1970-01-01 01:00:00:000');
  });
});
