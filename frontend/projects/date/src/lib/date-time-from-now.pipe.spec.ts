import {DateTimeFromNowPipe} from './date-time-from-now.pipe';

describe('DateTimeFromNowPipe', () => {
  it('create an instance', () => {
    const pipe = new DateTimeFromNowPipe();
    expect(pipe).toBeTruthy();
    expect(pipe.transform(0)).toBeDefined();
  });
});
