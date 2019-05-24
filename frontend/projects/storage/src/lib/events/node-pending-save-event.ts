import {BusEvent} from 'projects/event/src/lib/bus-event';
import {StorageNode} from 'projects/storage/src/lib/entities/storage-node';

export class NodePendingSaveEvent extends BusEvent {

  public static readonly CHANNEL = 'storage-node-pending-save';

  constructor(
    public readonly node: StorageNode,
    public readonly pendingSave: boolean) {
    super(NodePendingSaveEvent.CHANNEL);
  }
}
