import {TaskType} from 'projects/runtime/src/lib/entities/task-type';
import {ExecutionEnvironmentEntry} from 'projects/runtime/src/lib/entities/execution-environment-entry';

export class ExecutionEnvironment {

  constructor(public readonly taskType: TaskType,
              public readonly description: string,
              public readonly hostIds: string[],
              public readonly entries: ExecutionEnvironmentEntry[],
  ) {
  }
}
