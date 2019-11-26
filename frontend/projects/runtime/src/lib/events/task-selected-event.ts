import {BusEvent} from 'projects/event/src/lib/bus-event';
import {Task} from 'projects/runtime/src/lib/entities/task';

export class TaskSelectedEvent extends BusEvent {

  public static readonly CHANNEL = 'task-selected-event';

  constructor(public readonly task?: Task) {
    super(TaskSelectedEvent.CHANNEL);
  }
}
