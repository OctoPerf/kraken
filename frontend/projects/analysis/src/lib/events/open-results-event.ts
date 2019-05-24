import {BusEvent} from 'projects/event/src/lib/bus-event';

export class OpenResultsEvent extends BusEvent {

  public static readonly CHANNEL = 'open-gatling-results';

  constructor() {
    super(OpenResultsEvent.CHANNEL);
  }

}

