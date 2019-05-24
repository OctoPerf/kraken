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
import {DockerComposeEditorComponent} from 'projects/docker/src/lib/docker-compose/docker-compose-editor/docker-compose-editor.component';
import {CommandModule} from 'projects/command/src/lib/command.module';
import {ContextualMenuModule} from 'projects/administration/src/app/contextual-menu/contextual-menu.module';

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
      'administration-storage',
      [{
        regexp: 'docker-compose.yml',
        editor: DockerComposeEditorComponent
      }]
    ),
    DockerModule.forRoot(
      'administration-docker'
    ),
    CommandModule,
    ContextualMenuModule,
  ],
  providers: [
    NotificationService,
    TabsService,
  ],
})
export class WorkspaceModule {
  constructor(public tabsService: TabsService) {
    // inject services to force initialization
  }
}
