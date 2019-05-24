import { WorkspacesModule } from './workspaces.module';

describe('WorkspacesModule', () => {
  let ideModule: WorkspacesModule;

  beforeEach(() => {
    ideModule = new WorkspacesModule();
  });

  it('should create an instance', () => {
    expect(ideModule).toBeTruthy();
  });
});
