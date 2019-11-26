import {ContainerStatus} from 'projects/runtime/src/lib/entities/container-status';
import {TaskType} from 'projects/runtime/src/lib/entities/task-type';
import {Container} from 'projects/runtime/src/lib/entities/container';

export interface Task {
  readonly id: string;
  readonly startDate: number;
  readonly status: ContainerStatus;
  readonly type: TaskType;
  readonly containers: Container[];
  readonly expectedCount: number;
  readonly description: number;
}
