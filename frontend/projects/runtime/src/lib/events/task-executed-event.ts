import {BusEvent} from 'projects/event/src/lib/bus-event';
import {ExecutionContext} from 'projects/runtime/src/lib/entities/execution-context';

export class TaskExecutedEvent extends BusEvent {

  public static readonly CHANNEL = 'task-executed-event';

  constructor(public readonly taskId: string, public readonly context: ExecutionContext) {
    super(TaskExecutedEvent.CHANNEL);
  }
}
