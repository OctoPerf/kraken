import {BusEvent} from 'projects/event/src/lib/bus-event';
import {CommandLog} from 'projects/command/src/lib/entities/command-log';

export class CommandLogEvent extends BusEvent {

  public static readonly CHANNEL = 'command-log-event';

  constructor(public readonly log: CommandLog) {
    super(CommandLogEvent.CHANNEL);
  }
}
