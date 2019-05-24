import {BusEvent} from 'projects/event/src/lib/bus-event';

export class SplitPanesDragStart extends BusEvent {

  public static readonly CHANNEL = 'split-drag-start';

  constructor() {
    super(SplitPanesDragStart.CHANNEL);
  }
}

