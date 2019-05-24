import {HighlightModule} from 'projects/help/src/lib/highlight/highlight.module';

describe('HighlightModule', () => {
  let highlightModule: HighlightModule;

  beforeEach(() => {
    highlightModule = new HighlightModule();
  });

  it('should create an instance', () => {
    expect(highlightModule).toBeTruthy();
  });
});
