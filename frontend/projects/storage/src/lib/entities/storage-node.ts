export type StorageNodeType = 'FILE' | 'DIRECTORY' | 'NONE';

export interface StorageNode {
  path: string;
  type: StorageNodeType;
  depth: number;
  length: number;
  lastModified: number;
}
