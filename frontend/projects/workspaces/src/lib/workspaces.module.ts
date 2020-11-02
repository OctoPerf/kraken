import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {VendorsModule} from 'projects/vendors/src/lib/vendors.module';
import {WorkspaceLayoutComponent} from 'projects/workspaces/src/lib/workspace-layout.component';
import {TabsModule} from 'projects/tabs/src/lib/tabs.module';
import {SplitModule} from 'projects/split/src/lib/split.module';
import {SideSplitComponent} from 'projects/workspaces/src/lib/side-split/side-split.component';

@NgModule({
  imports: [
    CommonModule,
    TabsModule,
    SplitModule,
    VendorsModule,
  ],
  declarations: [
    SideSplitComponent,
    WorkspaceLayoutComponent,
  ],
  exports: [
    SideSplitComponent,
    WorkspaceLayoutComponent,
  ]
})
export class WorkspacesModule { }
