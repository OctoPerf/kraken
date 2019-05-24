import {BusEvent} from 'projects/event/src/lib/bus-event';
import {StorageNode} from 'projects/storage/src/lib/entities/storage-node';

export class ContextualMenuEvent extends BusEvent {

  public static readonly CHANNEL = 'storage-contextual-menu';

  constructor(
    public readonly $event: MouseEvent,
    public readonly storageId: string,
    public readonly node?: StorageNode,
  ) {
    super(ContextualMenuEvent.CHANNEL);
  }
}
