import {BusEvent} from 'projects/event/src/lib/bus-event';

export class OpenStorageTreeEvent extends BusEvent {

  public static readonly CHANNEL = 'open-storage-tree';

  constructor() {
    super(OpenStorageTreeEvent.CHANNEL);
  }
}
