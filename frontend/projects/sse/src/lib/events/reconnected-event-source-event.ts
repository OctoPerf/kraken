import {BusEvent} from 'projects/event/src/lib/bus-event';

export class ReconnectedEventSourceEvent extends BusEvent {

  public static readonly CHANNEL = 'reconnected-event-source-event';

  constructor() {
    super(ReconnectedEventSourceEvent.CHANNEL);
  }
}
