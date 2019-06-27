import {BusEvent} from 'projects/event/src/lib/bus-event';
import {Tab} from 'projects/tabs/src/lib/tab';

export class TabUnselectedEvent extends BusEvent {

  public static readonly CHANNEL = 'tab-unselected';

  constructor(public tab: Tab) {
    super(TabUnselectedEvent.CHANNEL);
  }
}
