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
import {DockerModule} from 'projects/docker/src/lib/docker.module';
import {CommandModule} from 'projects/command/src/lib/command.module';
import {SimulationsModule} from 'projects/gatling/src/app/simulations/simulations.module';
import {SimulationEditorComponent} from 'projects/gatling/src/app/simulations/simulation-editor/simulation-editor.component';
import {ResultsModule} from 'projects/analysis/src/lib/results/results.module';
import {DebugEditorComponent} from 'projects/analysis/src/lib/results/debug/debug-editor/debug-editor.component';
import {StorageWatcherEvent} from 'projects/storage/src/lib/entities/storage-watcher-event';
import {StorageWatcherService} from 'projects/storage/src/lib/storage-watcher.service';
import {ResultsListService} from 'projects/analysis/src/lib/results/results-list.service';

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
        editor: SimulationEditorComponent
      },
        {
          regexp: '.*\\.debug',
          editor: DebugEditorComponent
        }]
    ),
    DockerModule.forRoot(
      'gatling-docker'
    ),
    CommandModule,
    SimulationsModule,
    ResultsModule,
  ],
  providers: [
    NotificationService,
    TabsService,
  ],
})
export class WorkspaceModule {
  constructor(public tabsService: TabsService, results: ResultsListService) {
    // inject services to force initialization
    results.init();
  }
}
