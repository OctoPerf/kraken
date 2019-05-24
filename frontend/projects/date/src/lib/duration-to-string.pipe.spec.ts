import {DurationToStringPipe} from 'projects/date/src/lib/duration-to-string.pipe';

describe('DurationToStringPipe', () => {
  it('create an instance', () => {
    const pipe = new DurationToStringPipe();
    expect(pipe).toBeTruthy();
  });

  it('format date', () => {
    const pipe = new DurationToStringPipe();
    expect(pipe.transform(0)).toEqual('a few seconds');
    expect(pipe.transform(1000)).toEqual('a few seconds');
    expect(pipe.transform(2000)).toEqual('a few seconds');
    expect(pipe.transform(60000)).toEqual('a minute');
    expect(pipe.transform(120000)).toEqual('2 minutes');
  });
});
