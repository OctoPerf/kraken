import {Pipe, PipeTransform} from '@angular/core';
import {TaskType} from 'projects/runtime/src/lib/entities/task-type';

@Pipe({
  name: 'taskTypeToString'
})
export class TaskTypeToStringPipe implements PipeTransform {

  static readonly TYPES: { [key in TaskType]: string } = {
    GATLING_RUN: 'Run',
    GATLING_DEBUG: 'Debug',
    GATLING_RECORD: 'Record',
  };

  transform(taskType: TaskType): string {
    return TaskTypeToStringPipe.TYPES[taskType];
  }

}
