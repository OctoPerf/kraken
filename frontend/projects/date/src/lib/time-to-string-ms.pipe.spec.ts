import {TimeToStringMsPipe} from 'projects/date/src/lib/time-to-string-ms.pipe';

describe('TimeToStringMsPipe', () => {
  it('create an instance', () => {
    const pipe = new TimeToStringMsPipe();
    expect(pipe).toBeTruthy();
  });

  it('format date', () => {
    const pipe = new TimeToStringMsPipe();
    expect(pipe.transform(0)).toEqual('01:00:00:000');
  });
});
