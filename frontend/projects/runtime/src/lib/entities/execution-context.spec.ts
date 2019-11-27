import {ExecutionContext} from 'projects/runtime/src/lib/entities/execution-context';

export const testExecutionContext: () => ExecutionContext = () => {
  return new ExecutionContext(
    'RUN',
    'description',
    {foo: 'bar'},
    {hostId: {key: 'value'}},
  );
};
