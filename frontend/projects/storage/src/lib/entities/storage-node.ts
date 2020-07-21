export type StorageNodeType = 'FILE' | 'DIRECTORY' | 'NONE';

export interface StorageNode {
  path: string;
  type: StorageNodeType;
  depth: number;
  length: number;
  lastModified: number;
}

export const ROOT_NODE: StorageNode = {
  path: '',
  type: 'DIRECTORY',
  depth: -1,
  length: 0,
  lastModified: 0,
};
