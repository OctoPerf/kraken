import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {RuntimeTaskModule} from 'projects/runtime/src/lib/runtime-task/runtime-task.module';


@NgModule({
  declarations: [],
  imports: [RuntimeTaskModule],
  providers: [
    RuntimeLogModule,
  ]
})
export class RuntimeLogModule {
}
