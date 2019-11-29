import {NgModule} from '@angular/core';
import {RuntimeContainerService} from 'projects/runtime/src/lib/runtime-task/runtime-container.service';
import {RuntimeTaskService} from 'projects/runtime/src/lib/runtime-task/runtime-task.service';
import {TaskTableComponent} from './task-table/task-table.component';


@NgModule({
  declarations: [
    TaskTableComponent
  ],
  imports: [],
  exports: [
    TaskTableComponent,
  ],
  providers: [
    RuntimeContainerService,
    RuntimeTaskService,
  ]
})
export class RuntimeTaskModule {
}
