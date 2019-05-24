import {BusEvent} from 'projects/event/src/lib/bus-event';
import {StorageNode} from 'projects/storage/src/lib/entities/storage-node';

export class SelectNodeEvent extends BusEvent {

  public static readonly CHANNEL = 'select-storage-node';

  constructor(
    public readonly node: StorageNode) {
    super(SelectNodeEvent.CHANNEL);
  }
}
