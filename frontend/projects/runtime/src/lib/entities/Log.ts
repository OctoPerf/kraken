import {LogType} from 'projects/runtime/src/lib/entities/LogType';

export interface Log {
  applicationId: string;
  id: string;
  type: LogType;
  text: string;
}
