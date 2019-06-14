import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {ImageNameDialogComponent} from './image-name-dialog/image-name-dialog.component';
import {VendorsModule} from 'projects/vendors/src/lib/vendors.module';
import {RunContainerDialogComponent} from './run-container-dialog/run-container-dialog.component';
import {EditorModule} from 'projects/editor/src/lib/editor.module';
import { SystemPruneDialogComponent } from './system-prune-dialog/system-prune-dialog.component';
import {ComponentsModule} from 'projects/components/src/lib/components.module';
import {HelpModule} from 'projects/help/src/lib/help.module';

@NgModule({
  imports: [
    CommonModule,
    VendorsModule,
    EditorModule,
    ComponentsModule,
    HelpModule,
  ],
  declarations: [ImageNameDialogComponent, RunContainerDialogComponent, SystemPruneDialogComponent],
  exports: [ImageNameDialogComponent, RunContainerDialogComponent, SystemPruneDialogComponent],
  entryComponents: [ImageNameDialogComponent, RunContainerDialogComponent, SystemPruneDialogComponent],
})
export class DockerDialogsModule { }
