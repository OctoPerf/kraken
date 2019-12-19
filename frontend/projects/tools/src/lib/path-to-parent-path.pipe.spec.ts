import {PathToParentPathPipe} from 'projects/tools/src/lib/path-to-parent-path.pipe';

describe('PathToParentPathPipe', () => {
  let pipe: PathToParentPathPipe;

  beforeEach(() => {
    pipe = new PathToParentPathPipe();
  });

  it('create an instance', () => {
    expect(pipe).toBeTruthy();
  });

  it('transform directory node', () => {
    expect(pipe.transform('root/folder')).toEqual('root');
  });

  it('transform css file node', () => {
    expect(pipe.transform('root/file.css')).toEqual('root');
  });
});
