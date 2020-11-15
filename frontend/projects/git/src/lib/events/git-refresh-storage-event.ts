import {BusEvent} from 'projects/event/src/lib/bus-event';

export class GitRefreshStorageEvent extends BusEvent {

  public static readonly CHANNEL = 'git-refresh-storage-event';

  constructor() {
    super(GitRefreshStorageEvent.CHANNEL);
  }
}
