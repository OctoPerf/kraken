import {EMPTY_TABS_CONFIG, TabsConfiguration} from './tabs-configuration';

describe('TabsConfiguration', () => {
  it('should create with min size', () => {
    const conf = new TabsConfiguration([], 0, 50);
    expect(conf.getInitSize(EMPTY_TABS_CONFIG)).toBe(100);
    expect(conf.getInitSize(conf)).toBe(50);
  });
});
