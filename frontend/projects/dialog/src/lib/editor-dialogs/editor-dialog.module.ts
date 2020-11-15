import {NgModule} from '@angular/core';
import {VendorsModule} from 'projects/vendors/src/lib/vendors.module';
import {CommonModule} from '@angular/common';
import {EditorModule} from 'projects/editor/src/lib/editor.module';
import {HelpModule} from 'projects/help/src/lib/help.module';
import {InspectDialogComponent} from 'projects/dialog/src/lib/editor-dialogs/inspect-dialog/inspect-dialog.component';
import {LogsDialogComponent} from 'projects/dialog/src/lib/editor-dialogs/logs-dialog/logs-dialog.component';

@NgModule({
  imports: [
    CommonModule,
    VendorsModule,
    EditorModule,
    HelpModule,
  ],
  declarations: [
    InspectDialogComponent,
    LogsDialogComponent,
  ],
  exports: [
    InspectDialogComponent,
    LogsDialogComponent,
  ],
})
export class EditorDialogsModule {
}
