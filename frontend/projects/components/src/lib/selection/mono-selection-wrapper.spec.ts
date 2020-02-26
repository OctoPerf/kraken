import {MonoSelectionWrapper} from 'projects/components/src/lib/selection/mono-selection-wrapper';

describe('MonoSelectionWrapper', () => {
  let wrapper: MonoSelectionWrapper<string>;

  beforeEach(() => {
    wrapper = new MonoSelectionWrapper<string>((t1, t2) => t1 === t2);
  });

  it('should have no selection', () => {
    expect(wrapper.hasSelection).toBeFalse();
    expect(wrapper.isSelected('test')).toBeFalse();
    expect(wrapper.selection).toBeNull();
  });

  it('should have selection', () => {
    wrapper.selection = 'test';
    expect(wrapper.hasSelection).toBeTrue();
    expect(wrapper.isSelected('test')).toBeTrue();
    expect(wrapper.selection).toBe('test');
  });

  it('should clear selection', () => {
    wrapper.selection = 'test';
    wrapper.selection = null;
    expect(wrapper.hasSelection).toBeFalse();
  });
});
