import {Log} from 'projects/runtime/src/lib/entities/log';

export const testLog: () => Log = () => {
  return {applicationId: 'applicationId', id: 'id', type: 'CONTAINER', text: 'text'};
};
