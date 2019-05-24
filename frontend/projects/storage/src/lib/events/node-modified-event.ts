import {StorageNode} from 'projects/storage/src/lib/entities/storage-node';
import {NodeEvent} from 'projects/storage/src/lib/events/node-event';

export class NodeModifiedEvent extends NodeEvent {

  public static readonly CHANNEL = 'storage-node-modified';

  constructor(node: StorageNode) {
    super(node, NodeModifiedEvent.CHANNEL);
  }
}
