export type ContainerStatus = 'CREATING'
  | 'STARTING'
  | 'PREPARING'
  | 'READY'
  | 'RUNNING'
  | 'STOPPING'
  | 'DONE'
  | 'FAILED';

export const isTerminalContainerStatus = (status: ContainerStatus) => status === 'DONE' || status === 'FAILED';
