import {StorageNode} from 'projects/storage/src/lib/entities/storage-node';

export interface StorageWatcherEvent {
  node: StorageNode;
  event: string;
}
