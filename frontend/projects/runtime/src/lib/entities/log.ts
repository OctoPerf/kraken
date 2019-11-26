import {LogType} from 'projects/runtime/src/lib/entities/log-type';

export interface Log {
  applicationId: string;
  id: string;
  type: LogType;
  text: string;
}
