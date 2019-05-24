import {BusEvent} from 'projects/event/src/lib/bus-event';
import {TabsSide} from 'projects/tabs/src/lib/tabs-side';
import {TabsPosition} from 'projects/tabs/src/lib/tabs-position';

export class TabsAddedEvent extends BusEvent {

  public static readonly CHANNEL = 'tabs-added';

  constructor(public side: TabsSide, public position: TabsPosition) {
    super(TabsAddedEvent.CHANNEL);
  }
}
