import { ColorToTextClassPipe } from './color-to-text-class.pipe';

describe('ColorToTextClassPipe', () => {
  it('create an instance', () => {
    const pipe = new ColorToTextClassPipe();
    expect(pipe).toBeTruthy();
    expect(pipe.transform('primary')).toBe('text-primary');
  });
});
