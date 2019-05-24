import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {ExecuteCommandDialogComponent} from './execute-command-dialog/execute-command-dialog.component';
import {VendorsModule} from 'projects/vendors/src/lib/vendors.module';
import {IconModule} from 'projects/icon/src/lib/icon.module';

@NgModule({
  declarations: [ExecuteCommandDialogComponent],
  entryComponents: [ExecuteCommandDialogComponent],
  exports: [ExecuteCommandDialogComponent],
  imports: [
    CommonModule,
    VendorsModule,
    IconModule,
  ]
})
export class CommandDialogsModule { }
