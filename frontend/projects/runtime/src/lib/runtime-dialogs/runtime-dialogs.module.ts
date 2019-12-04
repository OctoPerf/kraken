import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {HostsSelectorComponent} from './hosts-selector/hosts-selector.component';
import {VendorsModule} from 'projects/vendors/src/lib/vendors.module';
import {ExecutionDialogComponent} from './execution-dialog/execution-dialog.component';
import {PrettyStringPipe} from 'projects/tools/src/lib/pretty-string.pipe';
import {RuntimeHostService} from 'projects/runtime/src/lib/runtime-host/runtime-host.service';
import {RuntimeHostModule} from 'projects/runtime/src/lib/runtime-host/runtime-host.module';


@NgModule({
  declarations: [HostsSelectorComponent, ExecutionDialogComponent],
  imports: [
    CommonModule,
    VendorsModule,
    RuntimeHostModule
  ],
  exports: [
    ExecutionDialogComponent
  ],
  entryComponents: [
    ExecutionDialogComponent
  ],
  providers: [
    PrettyStringPipe
  ]
})
export class RuntimeDialogsModule {
}
