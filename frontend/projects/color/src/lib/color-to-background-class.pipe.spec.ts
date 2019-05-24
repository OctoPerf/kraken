import { ColorToBackgroundClassPipe } from './color-to-background-class.pipe';

describe('ColorToBackgroundClassPipe', () => {
  it('create an instance', () => {
    const pipe = new ColorToBackgroundClassPipe();
    expect(pipe).toBeTruthy();
    expect(pipe.transform('primary')).toBe('background-primary');
  });
});
