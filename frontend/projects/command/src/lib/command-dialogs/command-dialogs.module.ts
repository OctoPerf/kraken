import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {ExecuteCommandDialogComponent} from './execute-command-dialog/execute-command-dialog.component';
import {VendorsModule} from 'projects/vendors/src/lib/vendors.module';
import {IconModule} from 'projects/icon/src/lib/icon.module';
import {HelpModule} from 'projects/help/src/lib/help.module';

@NgModule({
  declarations: [ExecuteCommandDialogComponent],
  entryComponents: [ExecuteCommandDialogComponent],
  exports: [ExecuteCommandDialogComponent],
  imports: [
    CommonModule,
    VendorsModule,
    IconModule,
    HelpModule,
  ]
})
export class CommandDialogsModule { }
