import {BusEvent} from 'projects/event/src/lib/bus-event';
import {SplitPane} from 'projects/split/src/lib/split-pane';

export class SplitShownEvent extends BusEvent {

  public static readonly CHANNEL = 'split-shown';

  constructor(
    public id: string,
    public index: number,
    public pane: SplitPane,
  ) {
    super(SplitShownEvent.CHANNEL);
  }
}
