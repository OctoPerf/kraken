import {TaskType} from 'projects/runtime/src/lib/entities/TaskType';
import {ContainerStatus} from 'projects/runtime/src/lib/entities/ContainerStatus';

export interface FlatContainer {
  readonly id: string;
  readonly name: string;
  readonly hostId: string;
  readonly taskId: string;
  readonly taskType: TaskType;
  readonly label: string;
  readonly description: string;
  readonly startDate: number;
  readonly status: ContainerStatus;
  readonly expectedCount: number;
}
