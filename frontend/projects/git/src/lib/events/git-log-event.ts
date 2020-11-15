import {BusEvent} from 'projects/event/src/lib/bus-event';
import {Log} from 'projects/runtime/src/lib/entities/log';

export class GitLogEvent extends BusEvent {

  public static readonly CHANNEL = 'git-log-event';

  constructor(public readonly text: string) {
    super(GitLogEvent.CHANNEL);
  }
}
