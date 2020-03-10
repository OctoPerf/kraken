import {ExecutionEnvironment} from 'projects/runtime/src/lib/entities/execution-environment';
import {ExecutionEnvironmentEntry} from 'projects/runtime/src/lib/entities/execution-environment-entry';

export const testExecutionEnvironmentEntry: () => ExecutionEnvironmentEntry = () => {
  return new ExecutionEnvironmentEntry(
    '',
    'USER',
    'key',
    'value',
  );
};

export const testExecutionEnvironment: () => ExecutionEnvironment = () => {
  return new ExecutionEnvironment(
    'GATLING_RUN',
    'description',
    ['local'],
    [testExecutionEnvironmentEntry()],
  );
};
