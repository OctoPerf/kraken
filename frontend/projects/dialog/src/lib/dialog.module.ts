import {NgModule} from '@angular/core';
import {VendorsModule} from 'projects/vendors/src/lib/vendors.module';
import {InspectDialogComponent} from './inspect-dialog/inspect-dialog.component';
import {CommonModule} from '@angular/common';
import {EditorModule} from 'projects/editor/src/lib/editor.module';
import { DeleteDialogComponent } from './delete-dialog/delete-dialog.component';
import { LogsDialogComponent } from './logs-dialog/logs-dialog.component';
import { WaitDialogComponent } from './wait-dialog/wait-dialog.component';
import {HelpModule} from 'projects/help/src/lib/help.module';
import { ConfirmDialogComponent } from './confirm-dialog/confirm-dialog.component';
import {DialogService} from 'projects/dialog/src/lib/dialog.service';

@NgModule({
  imports: [
    CommonModule,
    VendorsModule,
    EditorModule,
    HelpModule,
  ],
  declarations: [
    InspectDialogComponent,
    DeleteDialogComponent,
    LogsDialogComponent,
    WaitDialogComponent,
    ConfirmDialogComponent,
  ],
  exports: [
    InspectDialogComponent,
    DeleteDialogComponent,
    LogsDialogComponent,
    WaitDialogComponent,
    ConfirmDialogComponent,
  ],
  entryComponents: [
    InspectDialogComponent,
    DeleteDialogComponent,
    LogsDialogComponent,
    WaitDialogComponent,
    ConfirmDialogComponent,
  ],
  providers: [
    DialogService,
  ]
})
export class DialogModule { }
