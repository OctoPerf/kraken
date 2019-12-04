import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {RuntimeWatcherService} from 'projects/runtime/src/lib/runtime-watcher/runtime-watcher.service';
import {RuntimeHostService} from 'projects/runtime/src/lib/runtime-host/runtime-host.service';

@NgModule({
  declarations: [],
  imports: [
    CommonModule
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
