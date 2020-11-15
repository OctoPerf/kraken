import {NgModule} from '@angular/core';
import {DefaultDialogsModule} from 'projects/dialog/src/lib/default-dialogs/default-dialog.module';
import {EditorDialogsModule} from 'projects/dialog/src/lib/editor-dialogs/editor-dialog.module';

@NgModule({
  imports: [
    DefaultDialogsModule,
    EditorDialogsModule,
  ],
  exports: [
    DefaultDialogsModule,
    EditorDialogsModule,
  ],
})
export class DialogModule { }
