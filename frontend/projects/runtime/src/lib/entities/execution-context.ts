import {TaskType} from 'projects/runtime/src/lib/entities/task-type';

export class ExecutionContext {

  constructor(public readonly taskType: TaskType,
              public readonly description: string,
              public readonly environment: { [key in string]: string },
              public readonly hosts: { [hostId in string]: { [key in string]: string } },
  ) {
  }
}
