import {NgModule} from '@angular/core';
import {CommonModule, JsonPipe} from '@angular/common';
import {ContainersTableComponent} from './containers-table/containers-table.component';
import {VendorsModule} from 'projects/vendors/src/lib/vendors.module';
import {EventModule} from 'projects/event/src/lib/event.module';
import {DialogModule} from 'projects/dialog/src/lib/dialog.module';
import {ImagesTableComponent} from './images-table/images-table.component';
import {ComponentsModule} from 'projects/components/src/lib/components.module';
import {ToolsModule} from 'projects/tools/src/lib/tools.module';
import {DockerService} from 'projects/docker/src/lib/docker-client/docker.service';
import {DateModule} from 'projects/date/src/lib/date.module';
import {IconModule} from 'projects/icon/src/lib/icon.module';
import {EditorModule} from 'projects/editor/src/lib/editor.module';
import {DockerDialogsModule} from 'projects/docker/src/lib/docker-dialogs/docker-dialogs.module';

@NgModule({
  declarations: [
    ContainersTableComponent,
    ImagesTableComponent,
  ],
  imports: [
    CommonModule,
    VendorsModule,
    EventModule,
    DialogModule,
    ComponentsModule,
    ToolsModule,
    DateModule,
    IconModule,
    EditorModule,
    DockerDialogsModule,
  ],
  exports: [
    ContainersTableComponent,
    ImagesTableComponent,
  ],
  entryComponents: [
    ContainersTableComponent,
    ImagesTableComponent,
  ],
  providers: [
    JsonPipe,
    DockerService,
  ]
})
export class DockerClientModule {
}
