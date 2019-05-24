import {StorageNode} from 'projects/storage/src/lib/entities/storage-node';
import {NodeEvent} from 'projects/storage/src/lib/events/node-event';

export class NodeDeletedEvent extends NodeEvent {

  public static readonly CHANNEL = 'storage-node-deleted';

  constructor(node: StorageNode) {
    super(node, NodeDeletedEvent.CHANNEL);
  }
}
