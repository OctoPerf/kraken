import {BusEvent} from 'projects/event/src/lib/bus-event';

export class LogsDetachedEvent extends BusEvent {

  public static readonly CHANNEL = 'logs-detached-event';

  constructor(public readonly logsId: string) {
    super(LogsDetachedEvent.CHANNEL);
  }
}
