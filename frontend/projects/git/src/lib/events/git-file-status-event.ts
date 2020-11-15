import {BusEvent} from 'projects/event/src/lib/bus-event';
import {Color} from 'projects/color/src/lib/color';

export class GitFileStatusEvent extends BusEvent {

  public static readonly CHANNEL = 'git-file-status-event';

  constructor(public readonly path: string,
              public readonly xy: string,
              public readonly color: Color = 'foreground',
  ) {
    super(GitFileStatusEvent.CHANNEL);
  }
}
