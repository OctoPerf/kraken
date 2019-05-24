import {BusEvent} from 'projects/event/src/lib/bus-event';

export class DeleteFilesEvent extends BusEvent {

  public static readonly CHANNEL = 'delete-files';

  constructor(
    public readonly results: boolean[]) {
    super(DeleteFilesEvent.CHANNEL);
  }
}
