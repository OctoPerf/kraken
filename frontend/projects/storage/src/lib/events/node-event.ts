import {BusEvent} from 'projects/event/src/lib/bus-event';
import {StorageNode} from 'projects/storage/src/lib/entities/storage-node';

export class NodeEvent extends BusEvent {

  constructor(
    public readonly node: StorageNode,
    public channel: string) {
    super(channel);
  }
}
