import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {RuntimeWatcherService} from 'projects/runtime/src/lib/runtime-watcher/runtime-watcher.service';
import {SSEModule} from 'projects/sse/src/lib/sse.module';

@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    SSEModule
  ],
  providers: [
    RuntimeWatcherService,
  ]
})
export class RuntimeWatcherModule {
  constructor(watcher: RuntimeWatcherService) {
    // inject services to force initialization
  }
}
