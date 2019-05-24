import { ColorToFillClassPipe } from './color-to-fill-class.pipe';

describe('ColorToFillClassPipe', () => {
  it('create an instance', () => {
    const pipe = new ColorToFillClassPipe();
    expect(pipe).toBeTruthy();
    expect(pipe.transform('primary')).toBe('fill-primary');
  });
});
