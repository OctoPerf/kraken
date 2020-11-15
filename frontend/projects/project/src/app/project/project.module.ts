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
import {ImportProjectComponent} from './import-project/import-project.component';
import {ProjectNameInputComponent} from './project-name-input/project-name-input.component';
import {ApplicationInputComponent} from './application-input/application-input.component';
import {GitUserModule} from 'projects/git/src/lib/git-user/git-user.module';
import {GitProjectModule} from 'projects/git/src/lib/git-project/git-project.module';

@NgModule({
  declarations: [
    ProjectListComponent,
    CreateProjectComponent,
    WelcomeComponent,
    ProjectItemComponent,
    ImportProjectComponent,
    ProjectNameInputComponent,
    ApplicationInputComponent,
  ],
  exports: [
    ProjectListComponent,
    CreateProjectComponent,
    ImportProjectComponent,
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
    GitUserModule,
    GitProjectModule,
  ],
})
export class ProjectModule {
}
