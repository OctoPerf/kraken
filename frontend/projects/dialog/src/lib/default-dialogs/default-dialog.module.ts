import {NgModule} from '@angular/core';
import {VendorsModule} from 'projects/vendors/src/lib/vendors.module';
import {CommonModule} from '@angular/common';
import {DeleteDialogComponent} from './delete-dialog/delete-dialog.component';
import {WaitDialogComponent} from './wait-dialog/wait-dialog.component';
import {HelpModule} from 'projects/help/src/lib/help.module';
import {ConfirmDialogComponent} from './confirm-dialog/confirm-dialog.component';

@NgModule({
  imports: [
    CommonModule,
    VendorsModule,
    HelpModule,
  ],
  declarations: [
    DeleteDialogComponent,
    WaitDialogComponent,
    ConfirmDialogComponent,
  ],
  exports: [
    DeleteDialogComponent,
    WaitDialogComponent,
    ConfirmDialogComponent,
  ],
})
export class DefaultDialogsModule {
}
