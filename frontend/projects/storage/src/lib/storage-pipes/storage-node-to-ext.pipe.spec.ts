import { StorageNodeToExtPipe } from './storage-node-to-ext.pipe';
import {
  testStorageDirectoryNode,
  testStorageFileNode,
  testStorageRootNode
} from 'projects/storage/src/lib/entities/storage-node.spec';

describe('StorageNodeToExtPipe', () => {
  it('create an instance', () => {
    const pipe = new StorageNodeToExtPipe();
    expect(pipe).toBeTruthy();
  });

  it('create filter directory', () => {
    const pipe = new StorageNodeToExtPipe();
    expect(pipe.transform(testStorageDirectoryNode())).toBe('');
  });

  it('create filter file', () => {
    const pipe = new StorageNodeToExtPipe();
    expect(pipe.transform(testStorageFileNode())).toBe('html');
  });

  it('create filter file', () => {
    const pipe = new StorageNodeToExtPipe();
    expect(pipe.transform(testStorageRootNode())).toBe('');
  });
});
