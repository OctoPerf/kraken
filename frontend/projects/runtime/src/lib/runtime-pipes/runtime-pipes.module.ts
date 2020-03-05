import {NgModule} from '@angular/core';
import {TaskTypeToStringPipe} from 'projects/runtime/src/lib/runtime-pipes/task-type-to-string.pipe';

@NgModule({
  exports: [
    TaskTypeToStringPipe,
  ],
  declarations: [TaskTypeToStringPipe],
})
export class RuntimePipesModule { }
