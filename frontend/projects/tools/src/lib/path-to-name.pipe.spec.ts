import {PathToNamePipe} from 'projects/tools/src/lib/path-to-name.pipe';

describe('PathToNamePipe', () => {
  let pipe: PathToNamePipe;

  beforeEach(() => {
    pipe = new PathToNamePipe();
  });

  it('create an instance', () => {
    expect(pipe).toBeTruthy();
  });

  it('transform directory node', () => {
    expect(pipe.transform('/root/folder')).toEqual('folder');
  });

  it('transform css file node', () => {
    expect(pipe.transform('/root/file.css')).toEqual('file.css');
  });
});
