import {NgModule} from '@angular/core';
import {MatDialogModule} from '@angular/material/dialog';
import {InjectDialogService} from 'projects/dialog/src/lib/inject-dialogs/inject-dialog.service';

@NgModule({
  imports: [
    MatDialogModule
  ],
  providers: [
    InjectDialogService
  ]
})
export class InjectDialogsModule {
}
