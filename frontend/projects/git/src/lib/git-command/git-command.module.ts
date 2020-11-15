import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {GitCommandComponent} from './git-command/git-command.component';
import {GitStatusComponent} from './git-status/git-status.component';
import {ComponentsModule} from 'projects/components/src/lib/components.module';
import {VendorsModule} from 'projects/vendors/src/lib/vendors.module';
import {GitProjectModule} from 'projects/git/src/lib/git-project/git-project.module';
import {NotConnectedToGitComponent} from 'projects/git/src/lib/git-command/not-connected-to-git/not-connected-to-git.component';
import {EditorModule} from 'projects/editor/src/lib/editor.module';
import {GitWatcherModule} from 'projects/git/src/lib/git-watcher/git-watcher.module';
import {GitCommandService} from 'projects/git/src/lib/git-command/git-command.service';
import {IconModule} from 'projects/icon/src/lib/icon.module';
import {GitFileStatusTableComponent} from './git-file-status-table/git-file-status-table.component';
import {ToolsModule} from 'projects/tools/src/lib/tools.module';
import {GitRenamedCopiedStatusTableComponent} from 'projects/git/src/lib/git-command/git-renamed-copied-status-table/git-renamed-copied-status-table.component';
import {GitPathTableComponent} from 'projects/git/src/lib/git-command/git-path-table/git-path-table.component';
import {XyToStatusPipe} from './xy-to-status.pipe';
import {XyToColorPipe} from './xy-to-color.pipe';
import {GitStatusTabHeaderComponent} from './git-status-tab-header/git-status-tab-header.component';
import {ColorModule} from 'projects/color/src/lib/color.module';
import {GitRefreshStatusButtonComponent} from './git-refresh-status-button/git-refresh-status-button.component';

@NgModule({
  declarations: [
    GitCommandComponent,
    GitStatusComponent,
    NotConnectedToGitComponent,
    GitFileStatusTableComponent,
    GitRenamedCopiedStatusTableComponent,
    GitPathTableComponent,
    XyToStatusPipe,
    XyToColorPipe,
    GitStatusTabHeaderComponent,
    GitRefreshStatusButtonComponent,
  ],
  imports: [
    CommonModule,
    ComponentsModule,
    VendorsModule,
    GitProjectModule,
    EditorModule,
    GitWatcherModule,
    IconModule,
    ToolsModule,
    ColorModule,
  ],
  exports: [
    GitCommandComponent,
    GitStatusComponent,
    GitStatusTabHeaderComponent
  ],
  providers: [
    GitCommandService,
  ]
})
export class GitCommandModule {
}
