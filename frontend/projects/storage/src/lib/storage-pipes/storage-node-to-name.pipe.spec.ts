import {StorageNodeToNamePipe} from './storage-node-to-name.pipe';

describe('StorageNodeToNamePipe', () => {
  let pipe: StorageNodeToNamePipe;

  beforeEach(() => {
    pipe = new StorageNodeToNamePipe();
  });

  it('create an instance', () => {
    expect(pipe).toBeTruthy();
  });

  it('transform directory node', () => {
    expect(pipe.transform({
      path: '/root/folder',
      type: 'DIRECTORY',
      depth: 1, length: 0, lastModified: 0
    })).toEqual('folder');
  });

  it('transform css file node', () => {
    expect(pipe.transform({
      path: '/root/file.css',
      type: 'NONE',
      depth: 1, length: 42, lastModified: 1337
    })).toEqual('file.css');
  });
});
