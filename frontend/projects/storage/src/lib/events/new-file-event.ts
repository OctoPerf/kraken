import {BusEvent} from 'projects/event/src/lib/bus-event';
import {StorageNode} from 'projects/storage/src/lib/entities/storage-node';

export class NewFileEvent extends BusEvent {

  public static readonly CHANNEL = 'new-file';

  constructor(
    public readonly node: StorageNode) {
    super(NewFileEvent.CHANNEL);
  }
}
