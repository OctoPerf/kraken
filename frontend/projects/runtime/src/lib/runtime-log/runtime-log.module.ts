import {NgModule} from '@angular/core';
import {RuntimeTaskModule} from 'projects/runtime/src/lib/runtime-task/runtime-task.module';
import {RuntimeLogsComponent} from './runtime-logs/runtime-logs.component';
import {EditorModule} from 'projects/editor/src/lib/editor.module';
import {RuntimeLogsHeaderComponent} from './runtime-logs-header/runtime-logs-header.component';
import {VendorsModule} from 'projects/vendors/src/lib/vendors.module';
import {IconModule} from 'projects/icon/src/lib/icon.module';
import {RuntimeLogsPanelComponent} from './runtime-logs-panel/runtime-logs-panel.component';
import {ComponentsModule} from 'projects/components/src/lib/components.module';
import {TreeModule} from 'projects/tree/src/lib/tree.module';
import {CommonModule} from '@angular/common';
import {RuntimeLogService} from 'projects/runtime/src/lib/runtime-log/runtime-log.service';

@NgModule({
  declarations: [
    RuntimeLogsComponent,
    RuntimeLogsHeaderComponent,
    RuntimeLogsPanelComponent,
  ],
  imports: [
    CommonModule,
    RuntimeTaskModule,
    EditorModule,
    VendorsModule,
    IconModule,
    ComponentsModule,
    TreeModule,
  ],
  providers: [
    RuntimeLogService,
  ],
  exports: [
    RuntimeLogsPanelComponent,
  ],
  entryComponents: [
    RuntimeLogsComponent,
    RuntimeLogsPanelComponent,
  ]
})
export class RuntimeLogModule {
}
