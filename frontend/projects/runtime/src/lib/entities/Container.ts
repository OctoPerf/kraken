import {ContainerStatus} from 'projects/runtime/src/lib/entities/ContainerStatus';

export interface Container {
  readonly id: string;
  readonly name: string;
  readonly hostId: string;
  readonly label: string;
  readonly startDate: number;
  readonly status: ContainerStatus;
}
