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
import {RuntimeHostModule} from 'projects/runtime/src/lib/runtime-host/runtime-host.module';
import {RuntimeHostService} from 'projects/runtime/src/lib/runtime-host/runtime-host.service';
import {StorageWatcherService} from 'projects/storage/src/lib/storage-watcher.service';
import {HighlightModule} from 'projects/help/src/lib/highlight/highlight.module';
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
    HighlightModule,
    VendorsModule,
    WorkspacesModule,
    NotificationModule,
    HelpModule,
    IconModule,
    StorageModule.forRoot(
      'administration-storage',
      []
    ),
    RuntimeHostModule,
    SecurityModule,
  ],
  providers: [
    NotificationService,
    TabsService,
    RuntimeHostService,
  ],
})
export class WorkspaceModule {
  constructor(tabsService: TabsService,
              hostService: RuntimeHostService,
              storageWatcherService: StorageWatcherService,
              sseService: SSEService) {
    // inject services to force initialization
    hostService.hosts().subscribe();
  }
}
