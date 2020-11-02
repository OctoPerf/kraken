import {XyToColorPipe} from './xy-to-color.pipe';

describe('XyToColorPipe', () => {

  let pipe: XyToColorPipe;

  beforeEach(() => {
    pipe = new XyToColorPipe();
  });

  it('should create an instance', () => {
    expect(pipe).toBeTruthy();
  });

  it('should transform ..', () => {
    expect(pipe.transform('..')).toBe('foreground');
  });

  it('should transform M.', () => {
    expect(pipe.transform('M.')).toBe('info');
  });

  it('should transform .M', () => {
    expect(pipe.transform('.M')).toBe('info');
  });

  it('should transform AM', () => {
    expect(pipe.transform('AM')).toBe('info');
  });
});
