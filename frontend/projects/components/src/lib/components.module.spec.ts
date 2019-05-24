import {ComponentsModule} from 'projects/components/src/lib/components.module';

describe('ComponentsModule', () => {
  let layoutModule: ComponentsModule;

  beforeEach(() => {
    layoutModule = new ComponentsModule();
  });

  it('should create an instance', () => {
    expect(layoutModule).toBeTruthy();
  });
});
