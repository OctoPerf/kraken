import {NgModule} from '@angular/core';
import {RuntimeContainerService} from 'projects/runtime/src/lib/runtime-task/runtime-container.service';
import {RuntimeTaskService} from 'projects/runtime/src/lib/runtime-task/runtime-task.service';
import {TaskTableComponent} from './task-table/task-table.component';
import {ComponentsModule} from 'projects/components/src/lib/components.module';
import {VendorsModule} from 'projects/vendors/src/lib/vendors.module';
import {IconModule} from 'projects/icon/src/lib/icon.module';
import {DateModule} from 'projects/date/src/lib/date.module';
import {ToolsModule} from 'projects/tools/src/lib/tools.module';


@NgModule({
  declarations: [
    TaskTableComponent
  ],
  imports: [
    ComponentsModule,
    VendorsModule,
    IconModule,
    DateModule,
    ToolsModule,
  ],
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
