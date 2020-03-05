import {NgModule} from '@angular/core';
import {RuntimeTaskModule} from 'projects/runtime/src/lib/runtime-task/runtime-task.module';
import {RuntimeLogModule} from 'projects/runtime/src/lib/runtime-log/runtime-log.module';
import {RuntimeWatcherModule} from 'projects/runtime/src/lib/runtime-watcher/runtime-watcher.module';
import {RuntimeHostModule} from 'projects/runtime/src/lib/runtime-host/runtime-host.module';
import {RuntimePipesModule} from 'projects/runtime/src/lib/runtime-pipes/runtime-pipes.module';

@NgModule({
  imports: [
    RuntimeTaskModule,
    RuntimeLogModule,
    RuntimeWatcherModule,
    RuntimeHostModule,
    RuntimePipesModule,
  ],
  exports: [
    RuntimeTaskModule,
    RuntimeLogModule,
    RuntimeWatcherModule,
    RuntimeHostModule,
    RuntimePipesModule,
  ],
})
export class RuntimeModule {
}
