import {EditorModule} from 'projects/editor/src/lib/editor.module';

describe('EditorModule', () => {
  let codeModule: EditorModule;

  beforeEach(() => {
    codeModule = new EditorModule();
  });

  it('should create an instance', () => {
    expect(codeModule).toBeTruthy();
  });
});
