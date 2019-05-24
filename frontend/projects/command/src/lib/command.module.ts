import {NgModule} from '@angular/core';
import {CommandLogsComponent} from './command-logs/command-logs.component';
import {CommandTabsPanelComponent} from './command-tabs-panel/command-tabs-panel.component';
import {CommandTabHeaderComponent} from './command-tab-header/command-tab-header.component';
import {CommonModule} from '@angular/common';
import {VendorsModule} from 'projects/vendors/src/lib/vendors.module';
import {EventModule} from 'projects/event/src/lib/event.module';
import {IconModule} from 'projects/icon/src/lib/icon.module';
import {EditorModule} from 'projects/editor/src/lib/editor.module';
import {ComponentsModule} from 'projects/components/src/lib/components.module';
import {CommandService} from 'projects/command/src/lib/command.service';
import {CommandDialogsModule} from 'projects/command/src/lib/command-dialogs/command-dialogs.module';
import {DialogModule} from 'projects/dialog/src/lib/dialog.module';
import {ToolsModule} from 'projects/tools/src/lib/tools.module';
import {TreeModule} from 'projects/tree/src/lib/tree.module';

@NgModule({
  imports: [
    CommonModule,
    VendorsModule,
    EventModule,
    IconModule,
    EditorModule,
    ComponentsModule,
    DialogModule,
    ToolsModule,
    CommandDialogsModule,
    TreeModule,
  ],
  declarations: [
    CommandLogsComponent,
    CommandTabsPanelComponent,
    CommandTabHeaderComponent,
  ],
  exports: [
    CommandLogsComponent,
    CommandTabsPanelComponent,
  ],
  entryComponents: [
    CommandLogsComponent,
    CommandTabsPanelComponent,
  ],
  providers: [
    CommandService,
  ]
})
export class CommandModule {
}
