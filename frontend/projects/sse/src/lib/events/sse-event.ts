import {BusEvent} from 'projects/event/src/lib/bus-event';
import {SSEWrapper} from 'projects/sse/src/lib/entities/sse-wrapper';

export class SSEEvent extends BusEvent {

  public static readonly CHANNEL = 'sse-event';

  constructor(public readonly wrapper: SSEWrapper) {
    super(SSEEvent.CHANNEL);
  }
}
