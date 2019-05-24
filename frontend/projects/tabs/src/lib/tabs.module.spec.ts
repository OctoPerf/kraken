import {TabsModule} from 'projects/tabs/src/lib/tabs.module';

describe('TabsModule', () => {
  let tabsModule: TabsModule;

  beforeEach(() => {
    tabsModule = new TabsModule();
  });

  it('should create an instance', () => {
    expect(tabsModule).toBeTruthy();
  });
});
