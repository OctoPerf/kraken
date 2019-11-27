import {NgModule} from '@angular/core';
import {RuntimeContainerService} from 'projects/runtime/src/lib/runtime-task/runtime-container.service';
import {RuntimeTaskService} from 'projects/runtime/src/lib/runtime-task/runtime-task.service';


@NgModule({
  declarations: [],
  imports: [],
  providers: [
    RuntimeContainerService,
    RuntimeTaskService,
  ]
})
export class RuntimeTaskModule { }
