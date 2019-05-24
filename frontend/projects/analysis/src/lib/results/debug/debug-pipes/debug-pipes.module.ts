import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {DebugChunkToPathPipe} from 'projects/analysis/src/lib/results/debug/debug-pipes/debug-chunk-to-path.pipe';
import {DebugChunkToStringPipe} from 'projects/analysis/src/lib/results/debug/debug-pipes/debug-chunk-to-string.pipe';
import {StorageModule} from 'projects/storage/src/lib/storage.module';
import {DateModule} from 'projects/date/src/lib/date.module';

@NgModule({
  declarations: [
    DebugChunkToPathPipe,
    DebugChunkToStringPipe,
  ],
  imports: [
    CommonModule,
    StorageModule,
    DateModule,
  ],
  providers: [
    DebugChunkToPathPipe,
    DebugChunkToStringPipe,
  ]
})
export class DebugPipesModule { }
