import {SideConfiguration} from './side-configuration';
import {EMPTY_TABS_CONFIG} from './tabs-configuration';

describe('SideConfiguration', () => {
  it('should create with min size', () => {
    const conf = new SideConfiguration(EMPTY_TABS_CONFIG, EMPTY_TABS_CONFIG, 42, 42);
    expect(conf.minSize).toBe(42);
  });
});
