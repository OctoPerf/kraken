import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {ExecuteSimulationDialogComponent} from 'projects/gatling/src/app/simulations/simulation-dialogs/execute-simulation-dialog/execute-simulation-dialog.component';
import {VendorsModule} from 'projects/vendors/src/lib/vendors.module';
import {ComponentsModule} from 'projects/components/src/lib/components.module';
import {IconModule} from 'projects/icon/src/lib/icon.module';
import { ImportHarDialogComponent } from 'projects/gatling/src/app/simulations/simulation-dialogs/import-har-dialog/import-har-dialog.component';

@NgModule({
  declarations: [ExecuteSimulationDialogComponent, ImportHarDialogComponent],
  exports: [ExecuteSimulationDialogComponent, ImportHarDialogComponent],
  entryComponents: [ExecuteSimulationDialogComponent, ImportHarDialogComponent],
  imports: [
    CommonModule,
    VendorsModule,
    ComponentsModule,
    IconModule,
    ComponentsModule,
  ]
})
export class SimulationDialogsModule { }
