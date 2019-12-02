import {BusEvent} from 'projects/event/src/lib/bus-event';

export class OpenLogsEvent extends BusEvent {

  public static readonly CHANNEL = 'open-logs-event';

  constructor() {
    super(OpenLogsEvent.CHANNEL);
  }
}
