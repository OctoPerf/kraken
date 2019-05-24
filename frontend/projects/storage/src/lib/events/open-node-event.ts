import {BusEvent} from 'projects/event/src/lib/bus-event';
import {StorageNode} from 'projects/storage/src/lib/entities/storage-node';

export class OpenNodeEvent extends BusEvent {

  public static readonly CHANNEL = 'open-storage-node';

  constructor(
    public readonly node: StorageNode) {
    super(OpenNodeEvent.CHANNEL);
  }
}
