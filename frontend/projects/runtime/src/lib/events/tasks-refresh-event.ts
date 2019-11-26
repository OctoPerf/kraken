import {BusEvent} from 'projects/event/src/lib/bus-event';
import {Task} from 'projects/runtime/src/lib/entities/task';

export class TasksRefreshEvent extends BusEvent {

  public static readonly CHANNEL = 'tasks-refresh-event';

  constructor(public readonly tasks: Task[]) {
    super(TasksRefreshEvent.CHANNEL);
  }
}
