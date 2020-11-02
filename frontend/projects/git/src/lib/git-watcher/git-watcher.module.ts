import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {SSEModule} from 'projects/sse/src/lib/sse.module';
import {GitWatcherService} from 'projects/git/src/lib/git-watcher/git-watcher.service';

@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    SSEModule
  ],
  providers: [
    GitWatcherService,
  ]
})
export class GitWatcherModule {
  constructor(watcher: GitWatcherService) {
    // inject services to force initialization
  }
}
