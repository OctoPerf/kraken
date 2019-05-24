import {SplitPane} from 'projects/split/src/lib/split-pane';

describe('SplitPane', () => {
  it('should create an instance', () => {
    const split = new SplitPane(null, 42);
    expect(split.defaultSize).toBe(42);
    expect(split.minSize).toBe(0);
    expect(split.initSize).toBe(42);
  });

  it('should create another instance', () => {
    const split = new SplitPane(null, 42, 12, 54);
    expect(split.defaultSize).toBe(42);
    expect(split.minSize).toBe(12);
    expect(split.initSize).toBe(54);
  });
});
