import {BusEvent} from 'projects/event/src/lib/bus-event';

export class ReloadEventSourceEvent extends BusEvent {

  public static readonly CHANNEL = 'reload-event-source-event';

  constructor() {
    super(ReloadEventSourceEvent.CHANNEL);
  }
}
