import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {RepositoryUrlInputComponent} from 'projects/git/src/lib/git-project/repository-url-input/repository-url-input.component';
import {CurrentProjectComponent} from 'projects/git/src/lib/git-project/current-project/current-project.component';
import {ProjectMenuComponent} from 'projects/git/src/lib/git-project/project-menu/project-menu.component';
import {ConnectProjectDirective} from 'projects/git/src/lib/git-project/connect-project.directive';
import {DisconnectProjectDirective} from 'projects/git/src/lib/git-project/disconnect-project.directive';
import {VendorsModule} from 'projects/vendors/src/lib/vendors.module';
import {IconModule} from 'projects/icon/src/lib/icon.module';
import { ConnectProjectDialogComponent } from 'projects/git/src/lib/git-project/connect-project-dialog/connect-project-dialog.component';
import {GitUserModule} from 'projects/git/src/lib/git-user/git-user.module';
import {HelpModule} from 'projects/help/src/lib/help.module';

@NgModule({
  declarations: [
    RepositoryUrlInputComponent,
    CurrentProjectComponent,
    ProjectMenuComponent,
    ConnectProjectDirective,
    DisconnectProjectDirective,
    ConnectProjectDialogComponent
  ],
  imports: [
    CommonModule,
    VendorsModule,
    IconModule,
    GitUserModule,
    HelpModule
  ],
  exports: [
    RepositoryUrlInputComponent,
    ProjectMenuComponent,
    ConnectProjectDirective,
    DisconnectProjectDirective,
  ]
})
export class GitProjectModule {
}
