import {StorageNodeToPredicatePipe} from './storage-node-to-predicate.pipe';
import {testStorageDirectoryNode, testStorageFileNode} from 'projects/storage/src/lib/entities/storage-node.spec';

describe('StorageNodeToPredicatePipe', () => {

  let pipe: StorageNodeToPredicatePipe;

  beforeEach(() => {
    pipe = new StorageNodeToPredicatePipe();
  });

  it('create an instance', () => {
    expect(pipe).toBeTruthy();
  });

  it('accept same node', () => {
    const predicate = pipe.transform(testStorageFileNode());
    expect(predicate(testStorageFileNode())).toBeTruthy();
  });

  it('accept parent node', () => {
    const predicate = pipe.transform(testStorageDirectoryNode());
    expect(predicate(testStorageFileNode())).toBeTruthy();
  });

  it('reject other node with close name', () => {
    const predicate = pipe.transform(testStorageDirectoryNode());
    expect(predicate({path: 'spotbugs.ext', type: 'FILE', depth: 0, length: 0, lastModified: 0})).toBeFalsy();
    expect(predicate({path: 'spotbugs.ext', type: 'FILE', depth: 1, length: 0, lastModified: 0})).toBeFalsy();
  });
});
