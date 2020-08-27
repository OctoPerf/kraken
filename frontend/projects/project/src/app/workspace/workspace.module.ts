import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {WorkspaceRoutingModule} from './workspace-routing.module';
import {WorkspaceComponent} from './workspace/workspace.component';
import {HighlightModule} from 'projects/help/src/lib/highlight/highlight.module';
import {SecurityModule} from 'projects/security/src/lib/security.module';
import {VendorsModule} from 'projects/vendors/src/lib/vendors.module';
import {HelpModule} from 'projects/help/src/lib/help.module';
import {SplitModule} from 'projects/split/src/lib/split.module';
import {ProjectModule} from 'projects/project/src/app/project/project.module';
import {IconModule} from 'projects/icon/src/lib/icon.module';
import {ProjectListResolverService} from 'projects/project/src/app/project/project-list-resolver.service';
import {DefaultDialogsModule} from 'projects/dialog/src/lib/default-dialogs/default-dialog.module';

@NgModule({
  declarations: [
    WorkspaceComponent,
  ],
  imports: [
    CommonModule,
    WorkspaceRoutingModule,
    HighlightModule,
    SecurityModule,
    VendorsModule,
    IconModule,
    HelpModule,
    SplitModule,
    DefaultDialogsModule,
    HelpModule,
    ProjectModule
  ],
  providers: [
    ProjectListResolverService
  ]
})
export class WorkspaceModule {
}
