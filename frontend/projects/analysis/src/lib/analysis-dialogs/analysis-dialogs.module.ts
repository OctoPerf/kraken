import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { OpenGatlingReportsDialogComponent } from './open-gatling-reports-dialog/open-gatling-reports-dialog.component';
import {VendorsModule} from 'projects/vendors/src/lib/vendors.module';
import {IconModule} from 'projects/icon/src/lib/icon.module';
import {StoragePipesModule} from 'projects/storage/src/lib/storage-pipes/storage-pipes.module';
import {ToolsModule} from 'projects/tools/src/lib/tools.module';
import {ComponentsModule} from 'projects/components/src/lib/components.module';



@NgModule({
  declarations: [OpenGatlingReportsDialogComponent],
  exports: [OpenGatlingReportsDialogComponent],
  imports: [
    CommonModule,
    VendorsModule,
    IconModule,
    StoragePipesModule,
    ToolsModule,
    ComponentsModule,
  ]
})
export class AnalysisDialogsModule { }
