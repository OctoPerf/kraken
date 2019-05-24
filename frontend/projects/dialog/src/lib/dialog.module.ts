import {NgModule} from '@angular/core';
import {VendorsModule} from 'projects/vendors/src/lib/vendors.module';
import {InspectDialogComponent} from './inspect-dialog/inspect-dialog.component';
import {CommonModule} from '@angular/common';
import {EditorModule} from 'projects/editor/src/lib/editor.module';
import { DeleteDialogComponent } from './delete-dialog/delete-dialog.component';
import { LogsDialogComponent } from './logs-dialog/logs-dialog.component';
import { WaitDialogComponent } from './wait-dialog/wait-dialog.component';

@NgModule({
  imports: [
    CommonModule,
    VendorsModule,
    EditorModule,
  ],
  declarations: [
    InspectDialogComponent,
    DeleteDialogComponent,
    LogsDialogComponent,
    WaitDialogComponent,
  ],
  exports: [
    InspectDialogComponent,
    DeleteDialogComponent,
    LogsDialogComponent,
    WaitDialogComponent,
  ],
  entryComponents: [
    InspectDialogComponent,
    DeleteDialogComponent,
    LogsDialogComponent,
    WaitDialogComponent,
  ]
})
export class DialogModule { }
