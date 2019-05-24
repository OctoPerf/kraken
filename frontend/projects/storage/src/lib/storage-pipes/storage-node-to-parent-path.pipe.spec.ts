import {StorageNodeToParentPathPipe} from './storage-node-to-parent-path.pipe';

describe('StorageNodeToParentPathPipe', () => {
  let pipe: StorageNodeToParentPathPipe;

  beforeEach(() => {
    pipe = new StorageNodeToParentPathPipe();
  });

  it('create an instance', () => {
    expect(pipe).toBeTruthy();
  });

  it('transform directory node', () => {
    expect(pipe.transform({
      path: 'root/folder',
      type: 'DIRECTORY',
      depth: 1, length: 0, lastModified: 0
    })).toEqual('root');
  });

  it('transform css file node', () => {
    expect(pipe.transform({
      path: 'root/file.css',
      type: 'NONE',
      depth: 1, length: 42, lastModified: 1337
    })).toEqual('root');
  });
});
