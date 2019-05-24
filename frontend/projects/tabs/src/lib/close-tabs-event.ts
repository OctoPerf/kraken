import {BusEvent} from 'projects/event/src/lib/bus-event';

export class CloseTabsEvent extends BusEvent {

  public static readonly CHANNEL = 'close-tabs';

  constructor() {
    super(CloseTabsEvent.CHANNEL);
  }
}
