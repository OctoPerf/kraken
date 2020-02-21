import {NgModule} from '@angular/core';
import {VendorsModule} from 'projects/vendors/src/lib/vendors.module';
import {CommonModule} from '@angular/common';
import {EditorModule} from 'projects/editor/src/lib/editor.module';
import {HelpModule} from 'projects/help/src/lib/help.module';
import {DialogService} from 'projects/dialog/src/lib/dialog.service';
import {DefaultDialogsModule} from 'projects/dialog/src/lib/default-dialogs/default-dialog.module';

@NgModule({
  imports: [
    CommonModule,
    VendorsModule,
    EditorModule,
    HelpModule,
    DefaultDialogsModule,
  ],
  declarations: [
  ],
  exports: [
    DefaultDialogsModule,
  ],
  entryComponents: [
  ],
  providers: [
    DialogService,
  ]
})
export class DialogModule { }
