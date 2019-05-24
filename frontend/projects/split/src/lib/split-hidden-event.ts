import {BusEvent} from 'projects/event/src/lib/bus-event';
import {SplitPane} from 'projects/split/src/lib/split-pane';

export class SplitHiddenEvent extends BusEvent {

  public static readonly CHANNEL = 'split-hidden';

  constructor(
    public id: string,
    public index: number,
    public pane: SplitPane,
  ) {
    super(SplitHiddenEvent.CHANNEL);
  }
}
