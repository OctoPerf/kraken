import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {AttachHostDialogComponent} from './attach-host-dialog/attach-host-dialog.component';
import {VendorsModule} from 'projects/vendors/src/lib/vendors.module';
import {HelpModule} from 'projects/help/src/lib/help.module';
import {SecurityModule} from 'projects/security/src/lib/security.module';


@NgModule({
  declarations: [AttachHostDialogComponent],
  exports: [
    AttachHostDialogComponent
  ],
  imports: [
    CommonModule,
    VendorsModule,
    HelpModule,
    SecurityModule
  ]
})
export class RuntimeHostDialogsModule {
}
