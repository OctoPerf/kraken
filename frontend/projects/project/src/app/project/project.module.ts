import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {ProjectListComponent} from './project-list/project-list.component';
import {CreateProjectComponent} from './create-project/create-project.component';
import {WelcomeComponent} from './welcome/welcome.component';
import {VendorsModule} from 'projects/vendors/src/lib/vendors.module';
import {RouterModule} from '@angular/router';
import {ComponentsModule} from 'projects/components/src/lib/components.module';
import {IconModule} from 'projects/icon/src/lib/icon.module';
import {ProjectItemComponent} from './project-item/project-item.component';
import {ToolsModule} from 'projects/tools/src/lib/tools.module';
import {DefaultDialogsModule} from 'projects/dialog/src/lib/default-dialogs/default-dialog.module';
import {HelpModule} from 'projects/help/src/lib/help.module';

@NgModule({
  declarations: [
    ProjectListComponent,
    CreateProjectComponent,
    WelcomeComponent,
    ProjectItemComponent,
  ],
  exports: [
    ProjectListComponent,
    CreateProjectComponent,
    WelcomeComponent,
  ],
  imports: [
    CommonModule,
    VendorsModule,
    RouterModule,
    ComponentsModule,
    IconModule,
    ToolsModule,
    DefaultDialogsModule,
    HelpModule,
  ],
})
export class ProjectModule {
}
