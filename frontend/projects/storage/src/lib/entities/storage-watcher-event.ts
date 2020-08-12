import {StorageNode} from 'projects/storage/src/lib/entities/storage-node';
import {Owner} from 'projects/security/src/lib/entities/owner';
import {StorageWatcherEventType} from 'projects/storage/src/lib/entities/storage-watcher-event-type';

export interface StorageWatcherEvent {
  node: StorageNode;
  type: StorageWatcherEventType;
  owner: Owner;
}
