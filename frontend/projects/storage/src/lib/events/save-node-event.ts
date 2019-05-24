import {BusEvent} from 'projects/event/src/lib/bus-event';
import {StorageNode} from 'projects/storage/src/lib/entities/storage-node';

export class SaveNodeEvent extends BusEvent {

  public static readonly CHANNEL = 'save-storage-node';

  constructor(
    public readonly node: StorageNode) {
    super(SaveNodeEvent.CHANNEL);
  }
}
