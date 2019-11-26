import {BusEvent} from 'projects/event/src/lib/bus-event';
import {Log} from 'projects/runtime/src/lib/entities/log';

export class LogEvent extends BusEvent {

  public static readonly CHANNEL = 'log-event';

  constructor(public readonly log: Log) {
    super(LogEvent.CHANNEL);
  }
}
