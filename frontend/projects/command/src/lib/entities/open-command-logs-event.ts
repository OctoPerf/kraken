import {BusEvent} from 'projects/event/src/lib/bus-event';

export class OpenCommandLogsEvent extends BusEvent {

  public static readonly CHANNEL = 'open-command-logs-event';

  constructor() {
    super(OpenCommandLogsEvent.CHANNEL);
  }
}
