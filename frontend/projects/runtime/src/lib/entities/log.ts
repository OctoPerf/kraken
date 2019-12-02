import {LogType} from 'projects/runtime/src/lib/entities/log-type';
import {LogStatus} from 'projects/runtime/src/lib/entities/log-status';

export interface Log {
  applicationId: string;
  id: string;
  type: LogType;
  text: string;
  status: LogStatus;
}
