import {BusEvent} from 'projects/event/src/lib/bus-event';

export class OpenTasksEvent extends BusEvent {

  public static readonly CHANNEL = 'open-tasks-event';

  constructor() {
    super(OpenTasksEvent.CHANNEL);
  }
}
