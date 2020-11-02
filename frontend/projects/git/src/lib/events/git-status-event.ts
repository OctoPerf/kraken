import {BusEvent} from 'projects/event/src/lib/bus-event';
import {GitStatus} from 'projects/git/src/lib/entities/git-status';

export class GitStatusEvent extends BusEvent {

  public static readonly CHANNEL = 'git-status-event';

  constructor(public readonly status: GitStatus) {
    super(GitStatusEvent.CHANNEL);
  }
}
