import {BusEvent} from 'projects/event/src/lib/bus-event';

export class TaskCancelledEvent extends BusEvent {

  public static readonly CHANNEL = 'task-cancelled-event';

  constructor(public readonly taskId: string) {
    super(TaskCancelledEvent.CHANNEL);
  }
}
