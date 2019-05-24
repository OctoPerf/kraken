export type ResultStatus = 'STARTING' | 'RUNNING' | 'COMPLETED' | 'CANCELED' | 'FAILED';
export type ResultType = 'RUN' | 'DEBUG' | 'HAR';

export interface Result {
  id: string;
  startDate: number;
  endDate: number;
  status: ResultStatus;
  runDescription: string;
  type: ResultType;
}
