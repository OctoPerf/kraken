import {NgModule} from '@angular/core';
import {RunSimulationNodeButtonComponent} from './run-simulation-node-button/run-simulation-node-button.component';
import {SimulationNodeButtonsComponent} from 'projects/gatling/src/app/simulations/simulation-node-buttons/simulation-node-buttons.component';
import {StorageMenuModule} from 'projects/storage/src/lib/storage-menu/storage-menu.module';
import {VendorsModule} from 'projects/vendors/src/lib/vendors.module';
import {IconModule} from 'projects/icon/src/lib/icon.module';
import {CommonModule} from '@angular/common';
import {SimulationContextualMenuComponent} from './simulation-contextual-menu/simulation-contextual-menu.component';
import {RunSimulationMenuItemComponent} from './run-simulation-menu-item/run-simulation-menu-item.component';
import {SimulationEditorComponent} from './simulation-editor/simulation-editor.component';
import {EditorModule} from 'projects/editor/src/lib/editor.module';
import {StorageNodeEditorsModule} from 'projects/storage/src/lib/storage-editor/storage-node-editors/storage-node-editors.module';
import {SimulationService} from 'projects/gatling/src/app/simulations/simulation.service';
import {DialogModule} from 'projects/dialog/src/lib/dialog.module';
import {DateModule} from 'projects/date/src/lib/date.module';
import {UploadHarMenuItemComponent} from './upload-har-menu-item/upload-har-menu-item.component';
import {ImportHarMenuItemComponent} from './import-har-menu-item/import-har-menu-item.component';
import {DebugSimulationMenuItemComponent} from 'projects/gatling/src/app/simulations/debug-simulation-menu-item/debug-simulation-menu-item.component';
import {DebugSimulationNodeButtonComponent} from 'projects/gatling/src/app/simulations/debug-simulation-node-button/debug-simulation-node-button.component';
import {SimulationDialogsModule} from 'projects/gatling/src/app/simulations/simulation-dialogs/simulation-dialogs.module';

@NgModule({
  imports: [
    CommonModule,
    VendorsModule,
    IconModule,
    StorageMenuModule,
    EditorModule,
    StorageNodeEditorsModule,
    DialogModule,
    DateModule
  ],
  declarations: [
    RunSimulationNodeButtonComponent,
    DebugSimulationNodeButtonComponent,
    SimulationNodeButtonsComponent,
    SimulationContextualMenuComponent,
    RunSimulationMenuItemComponent,
    DebugSimulationMenuItemComponent,
    SimulationEditorComponent,
    UploadHarMenuItemComponent,
    ImportHarMenuItemComponent,
  ],
  exports: [
    SimulationNodeButtonsComponent,
    SimulationContextualMenuComponent,
    SimulationEditorComponent,
    SimulationDialogsModule,
  ],
  providers: [
    SimulationService
  ]
})
export class SimulationsModule {
}
