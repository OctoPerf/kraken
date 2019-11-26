import {ContainerStatus} from 'projects/runtime/src/lib/entities/ContainerStatus';
import {TaskType} from 'projects/runtime/src/lib/entities/TaskType';
import {Container} from 'projects/runtime/src/lib/entities/Container';

export interface Task {
  readonly id: string;
  readonly startDate: number;
  readonly status: ContainerStatus;
  readonly type: TaskType;
  readonly containers: Container[];
  readonly expectedCount: number;
  readonly description: number;
}
