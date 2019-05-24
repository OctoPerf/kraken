import {BusEvent} from 'projects/event/src/lib/bus-event';

export class OpenNotificationsEvent extends BusEvent {

  public static readonly CHANNEL = 'open-notification';

  constructor() {
    super(OpenNotificationsEvent.CHANNEL);
  }
}
