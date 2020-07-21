import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {WorkspaceRoutingModule} from './workspace-routing.module';
import {WorkspaceComponent} from './workspace/workspace.component';
import {ComponentsModule} from 'projects/components/src/lib/components.module';
import {VendorsModule} from 'projects/vendors/src/lib/vendors.module';
import {WorkspacesModule} from 'projects/workspaces/src/lib/workspaces.module';
import {NotificationModule} from 'projects/notification/src/lib/notification.module';
import {HelpModule} from 'projects/help/src/lib/help.module';
import {StorageModule} from 'projects/storage/src/lib/storage.module';
import {IconModule} from 'projects/icon/src/lib/icon.module';
import {NotificationService} from 'projects/notification/src/lib/notification.service';
import {TabsService} from 'projects/tabs/src/lib/tabs.service';
import {SimulationsModule} from 'projects/gatling/src/app/simulations/simulations.module';
import {SimulationEditorComponent} from 'projects/gatling/src/app/simulations/simulation-editor/simulation-editor.component';
import {ResultsModule} from 'projects/analysis/src/lib/results/results.module';
import {DebugEditorComponent} from 'projects/analysis/src/lib/results/debug/debug-editor/debug-editor.component';
import {ResultsTableService} from 'projects/analysis/src/lib/results/results-table/results-table.service';
import {RuntimeHostService} from 'projects/runtime/src/lib/runtime-host/runtime-host.service';
import {RuntimeWatcherService} from 'projects/runtime/src/lib/runtime-watcher/runtime-watcher.service';
import {RuntimeModule} from 'projects/runtime/src/lib/runtime.module';
import {StorageWatcherService} from 'projects/storage/src/lib/storage-watcher.service';
import {SecurityModule} from 'projects/security/src/lib/security.module';
import {SSEService} from 'projects/sse/src/lib/sse.service';

@NgModule({
  declarations: [
    WorkspaceComponent,
  ],
  imports: [
    CommonModule,
    WorkspaceRoutingModule,
    ComponentsModule,
    VendorsModule,
    WorkspacesModule,
    NotificationModule,
    HelpModule,
    IconModule,
    StorageModule.forRoot(
      'gatling-storage',
      [{
        regexp: '.*\\.scala',
        editor: SimulationEditorComponent,
        helpPageId: 'EDITOR_GATLING'
      },
        {
          regexp: '.*\\.debug',
          editor: DebugEditorComponent,
          helpPageId: 'EDITOR_DEBUG'
        }]
    ),
    SimulationsModule,
    ResultsModule,
    RuntimeModule,
    SecurityModule,
  ],
  providers: [
    NotificationService,
    TabsService,
  ],
})
export class WorkspaceModule {
  constructor(tabsService: TabsService,
              results: ResultsTableService,
              hostService: RuntimeHostService,
              runtimeWatcherService: RuntimeWatcherService,
              storageWatcherService: StorageWatcherService,
              sseService: SSEService) {
    // inject services to force initialization
    results.init();
    hostService.hosts().subscribe();
  }
}
