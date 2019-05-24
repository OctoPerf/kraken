import {StorageNodeToIconPipe} from './storage-node-to-icon.pipe';
import {StorageNodeToExtPipe} from 'projects/storage/src/lib/storage-pipes/storage-node-to-ext.pipe';

describe('StorageNodeToIconPipe', () => {

  let pipe: StorageNodeToIconPipe;

  beforeEach(() => {
    pipe = new StorageNodeToIconPipe(new StorageNodeToExtPipe());
  });

  it('create an instance', () => {
    expect(pipe).toBeTruthy();
  });

  it('transform directory node', () => {
    expect(pipe.transform({
      path: '/root/folder',
      type: 'DIRECTORY',
      depth: 1, length: 0, lastModified: 0
    })).toEqual(StorageNodeToIconPipe.DEFAULT_DIRECTORY_ICON);
  });

  it('transform css file node', () => {
    expect(pipe.transform({
      path: '/root/file.css',
      type: 'NONE',
      depth: 1, length: 42, lastModified: 1337
    })).toEqual(StorageNodeToIconPipe.ICONS['css']);
  });

  it('transform other file node', () => {
    expect(pipe.transform({
      path: '/root/file.other',
      type: 'FILE',
      depth: 1, length: 42, lastModified: 1337
    })).toEqual(StorageNodeToIconPipe.DEFAULT_FILE_ICON);
  });
});
