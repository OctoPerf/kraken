import {BusEvent} from 'projects/event/src/lib/bus-event';

export class TabsContentInitializedEvent extends BusEvent {

  public static readonly CHANNEL = 'tabs-content-initialized';

  constructor(public content: any) {
    super(TabsContentInitializedEvent.CHANNEL);
  }
}
