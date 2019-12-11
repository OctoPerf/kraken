import {BusEvent} from 'projects/event/src/lib/bus-event';
import {Task} from 'projects/runtime/src/lib/entities/task';

export class TaskCancelledEvent extends BusEvent {

  public static readonly CHANNEL = 'task-cancelled-event';

  constructor(public readonly task: Task) {
    super(TaskCancelledEvent.CHANNEL);
  }
}
