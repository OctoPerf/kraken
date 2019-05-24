import { ToolsModule } from './tools.module';

describe('ToolsModule', () => {
  let toolsModule: ToolsModule;

  beforeEach(() => {
    toolsModule = new ToolsModule();
  });

  it('should create an instance', () => {
    expect(toolsModule).toBeTruthy();
  });
});
