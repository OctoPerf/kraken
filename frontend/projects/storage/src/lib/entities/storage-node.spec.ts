import {StorageNode} from 'projects/storage/src/lib/entities/storage-node';

export const testStorageFileNode: () => StorageNode = () => {
  return {path: 'spotbugs/main.html', type: 'FILE', depth: 1, length: 42, lastModified: 1337};
};

export const testStorageDirectoryNode: () => StorageNode = () => {
  return {path: 'spotbugs', type: 'DIRECTORY', depth: 0, length: 0, lastModified: 0};
};

export const testStorageRootNode: () => StorageNode = () =>  {
  return {path: '', type: 'DIRECTORY', depth: -1, length: 0, lastModified: 0};
};

export const testStorageNodes: () => StorageNode[] = () => {
  return [
    {path: 'spotbugs', type: 'DIRECTORY', depth: 0, length: 0, lastModified: 0},
    {path: 'spotbugs/main.html', type: 'FILE', depth: 1, length: 42, lastModified: 1337},
    {path: 'spotbugs/test.html', type: 'FILE', depth: 1, length: 42, lastModified: 1337},
    {path: 'report.js', type: 'FILE', depth: 0, length: 42, lastModified: 1337},
    {path: 'reports', type: 'DIRECTORY', depth: 0, length: 0, lastModified: 0},
    {path: 'reports/tests', type: 'DIRECTORY', depth: 1, length: 0, lastModified: 0},
    {path: 'reports/tests/test', type: 'DIRECTORY', depth: 2, length: 0, lastModified: 0},
    {path: 'reports/tests/test/index.html', type: 'FILE', depth: 3, length: 42, lastModified: 1337},
    {path: 'reports/tests/test/js', type: 'DIRECTORY', depth: 3, length: 0, lastModified: 0},
    {path: 'reports/tests/test/js/report.js', type: 'FILE', depth: 4, length: 42, lastModified: 1337},
    {path: 'reports/tests/test/css', type: 'DIRECTORY', depth: 3, length: 0, lastModified: 0},
    {path: 'reports/tests/test/css/base-style.css', type: 'FILE', depth: 4, length: 42, lastModified: 1337},
    {path: 'reports/tests/test/css/style.css', type: 'FILE', depth: 4, length: 42, lastModified: 1337},
  ];
};
