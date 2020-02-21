import {PathToCodeEditorModePipe} from './path-to-code-editor-mode.pipe';
import {TestBed} from '@angular/core/testing';
import {FormatterService} from 'projects/editor/src/lib/formatter.service';

describe('PathToCodeEditorModePipe', () => {

  let pipe: PathToCodeEditorModePipe;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [PathToCodeEditorModePipe]
    });
    pipe = TestBed.inject(PathToCodeEditorModePipe);
  });

  it('create an instance', () => {
    expect(pipe).toBeTruthy();
  });

  it('create an instance with custom matcher', () => {
    expect(new PathToCodeEditorModePipe([])).toBeTruthy();
  });

  it('administration.Dockerfile', () => {
    expect(pipe.transform('administration.Dockerfile')).toBe('dockerfile');
    expect(pipe.transform('/root/test/administration.Dockerfile')).toBe('dockerfile');
    expect(pipe.transform('/root/test/administration.Dockerfile.txt')).toBe('text');
  });

  it('css', () => {
    expect(pipe.transform('/root/test/file.css')).toBe('css');
    expect(pipe.transform('/root/test/file.css.txt')).toBe('text');
  });

  it('html', () => {
    expect(pipe.transform('/root/test/file.html')).toBe('html');
  });

  it('ini', () => {
    expect(pipe.transform('/root/test/file.ini')).toBe('ini');
  });

  it('markdown', () => {
    expect(pipe.transform('/root/test/file.markdown')).toBe('markdown');
    expect(pipe.transform('/root/test/file.md')).toBe('markdown');
  });

  it('Makefile', () => {
    expect(pipe.transform('Makefile')).toBe('makefile');
    expect(pipe.transform('/root/test/Makefile')).toBe('makefile');
    expect(pipe.transform('/root/test/Makefile.txt')).toBe('text');
  });

  it('other file', () => {
    expect(pipe.transform('/root/test/other.file')).toBe('text');
  });
});
