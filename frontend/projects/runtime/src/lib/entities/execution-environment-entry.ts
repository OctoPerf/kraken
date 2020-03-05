import {ExecutionEnvironmentEntrySource} from 'projects/runtime/src/lib/entities/execution-environment-entry-source';

export class ExecutionEnvironmentEntry {
  constructor(public readonly scope: string,
              public readonly from: ExecutionEnvironmentEntrySource,
              public readonly key: string,
              public readonly value: string,
  ) {
  }
}
