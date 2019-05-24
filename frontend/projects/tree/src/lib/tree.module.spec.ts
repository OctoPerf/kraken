import { TreeModule } from './tree.module';

describe('TreeModule', () => {
  let treeModule: TreeModule;

  beforeEach(() => {
    treeModule = new TreeModule();
  });

  it('should create an instance', () => {
    expect(treeModule).toBeTruthy();
  });
});
