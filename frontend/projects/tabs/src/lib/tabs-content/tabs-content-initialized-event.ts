import {BusEvent} from 'projects/event/src/lib/bus-event';
import {TabsContentComponent} from 'projects/tabs/src/lib/tabs-content/tabs-content.component';

export class TabsContentInitializedEvent extends BusEvent {

  public static readonly CHANNEL = 'tabs-content-initialized';

  constructor(public content: TabsContentComponent) {
    super(TabsContentInitializedEvent.CHANNEL);
  }
}
