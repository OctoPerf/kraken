import {BusEvent} from 'projects/event/src/lib/bus-event';
import {ExecutionEnvironment} from 'projects/runtime/src/lib/entities/execution-environment';

export class TaskExecutedEvent extends BusEvent {

  public static readonly CHANNEL = 'task-executed-event';

  constructor(public readonly taskId: string, public readonly context: ExecutionEnvironment) {
    super(TaskExecutedEvent.CHANNEL);
  }
}
