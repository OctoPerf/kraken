import {BusEvent} from 'projects/event/src/lib/bus-event';
import {Container} from 'projects/runtime/src/lib/entities/container';

export class LogsAttachedEvent extends BusEvent {

  public static readonly CHANNEL = 'logs-attached-event';

  constructor(public readonly logsId: string, public readonly container: Container) {
    super(LogsAttachedEvent.CHANNEL);
  }
}
