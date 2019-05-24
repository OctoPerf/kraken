import {BusEvent} from 'projects/event/src/lib/bus-event';

export class SplitPanesDragStop extends BusEvent {

  public static readonly CHANNEL = 'split-drag-stop';

  constructor() {
    super(SplitPanesDragStop.CHANNEL);
  }
}

