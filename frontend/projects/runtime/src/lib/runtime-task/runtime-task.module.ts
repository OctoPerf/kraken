import {NgModule} from '@angular/core';
import {RuntimeContainerService} from 'projects/runtime/src/lib/runtime-task/runtime-container.service';
import {RuntimeTaskService} from 'projects/runtime/src/lib/runtime-task/runtime-task.service';
import {TasksTableComponent} from 'projects/runtime/src/lib/runtime-task/tasks-table/tasks-table.component';
import {ComponentsModule} from 'projects/components/src/lib/components.module';
import {VendorsModule} from 'projects/vendors/src/lib/vendors.module';
import {IconModule} from 'projects/icon/src/lib/icon.module';
import {DateModule} from 'projects/date/src/lib/date.module';
import {ToolsModule} from 'projects/tools/src/lib/tools.module';


@NgModule({
  declarations: [
    TasksTableComponent
  ],
  imports: [
    ComponentsModule,
    VendorsModule,
    IconModule,
    DateModule,
    ToolsModule,
  ],
  exports: [
    TasksTableComponent,
  ],
  providers: [
    RuntimeContainerService,
    RuntimeTaskService,
  ]
})
export class RuntimeTaskModule {
}
