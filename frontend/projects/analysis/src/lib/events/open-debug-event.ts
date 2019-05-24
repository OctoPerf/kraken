import {BusEvent} from 'projects/event/src/lib/bus-event';

export class OpenDebugEvent extends BusEvent {

  public static readonly CHANNEL = 'open-gatling-debug-result';

  constructor() {
    super(OpenDebugEvent.CHANNEL);
  }

}

