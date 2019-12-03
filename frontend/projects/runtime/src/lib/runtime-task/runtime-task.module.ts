import {NgModule} from '@angular/core';
import {RuntimeContainerService} from 'projects/runtime/src/lib/runtime-task/runtime-container.service';
import {RuntimeTaskService} from 'projects/runtime/src/lib/runtime-task/runtime-task.service';
import {TasksTableComponent} from 'projects/runtime/src/lib/runtime-task/tasks-table/tasks-table.component';
import {ComponentsModule} from 'projects/components/src/lib/components.module';
import {VendorsModule} from 'projects/vendors/src/lib/vendors.module';
import {IconModule} from 'projects/icon/src/lib/icon.module';
import {DateModule} from 'projects/date/src/lib/date.module';
import {ToolsModule} from 'projects/tools/src/lib/tools.module';
import { ContainersTableComponent } from './containers-table/containers-table.component';
import {RuntimeHostModule} from 'projects/runtime/src/lib/runtime-host/runtime-host.module';


@NgModule({
  declarations: [
    TasksTableComponent,
    ContainersTableComponent
  ],
  imports: [
    ComponentsModule,
    VendorsModule,
    IconModule,
    DateModule,
    ToolsModule,
    RuntimeHostModule,
  ],
  exports: [
    TasksTableComponent,
    ContainersTableComponent
  ],
  providers: [
    RuntimeContainerService,
    RuntimeTaskService,
  ],
  entryComponents: [
    TasksTableComponent,
    ContainersTableComponent
  ]
})
export class RuntimeTaskModule {
}
