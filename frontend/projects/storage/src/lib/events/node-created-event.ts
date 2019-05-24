import {StorageNode} from 'projects/storage/src/lib/entities/storage-node';
import {NodeEvent} from 'projects/storage/src/lib/events/node-event';

export class NodeCreatedEvent extends NodeEvent {

  public static readonly CHANNEL = 'storage-node-created';

  constructor(node: StorageNode) {
    super(node, NodeCreatedEvent.CHANNEL);
  }
}
