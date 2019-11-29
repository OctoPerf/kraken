import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {StorageModule} from 'projects/storage/src/lib/storage.module';
import {DateModule} from 'projects/date/src/lib/date.module';
import {DebugEntryToPathPipe} from 'projects/analysis/src/lib/results/debug/debug-pipes/debug-entry-to-path.pipe';
import {DebugEntryToStringPipe} from 'projects/analysis/src/lib/results/debug/debug-pipes/debug-entry-to-string.pipe';

@NgModule({
  declarations: [
    DebugEntryToPathPipe,
    DebugEntryToStringPipe,
  ],
  imports: [
    CommonModule,
    StorageModule,
    DateModule,
  ],
  providers: [
    DebugEntryToPathPipe,
    DebugEntryToStringPipe,
  ]
})
export class DebugPipesModule { }
