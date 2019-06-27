import {BusEvent} from 'projects/event/src/lib/bus-event';
import {HelpEvent} from 'projects/help/src/lib/help-panel/help-event';
import {HelpPageId} from 'projects/help/src/lib/help-panel/help-page-id';

export class SelectHelpEvent extends BusEvent implements HelpEvent {

  public static readonly CHANNEL = 'select-help';

  constructor(public pageId?: HelpPageId) {
    super(SelectHelpEvent.CHANNEL);
  }
}
