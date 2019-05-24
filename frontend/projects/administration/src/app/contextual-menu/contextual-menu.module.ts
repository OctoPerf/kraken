import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {AdministrationContextualMenuComponent} from './administration-contextual-menu/administration-contextual-menu.component';
import {StorageMenuModule} from 'projects/storage/src/lib/storage-menu/storage-menu.module';
import {CommandModule} from 'projects/command/src/lib/command.module';
import {ExecuteCommandMenuItemComponent} from 'projects/administration/src/app/contextual-menu/execute-command-menu-item/execute-command-menu-item.component';
import {VendorsModule} from 'projects/vendors/src/lib/vendors.module';
import {IconModule} from 'projects/icon/src/lib/icon.module';
import { ExecuteScriptMenuItemComponent } from './execute-script-menu-item/execute-script-menu-item.component';
import {StoragePipesModule} from 'projects/storage/src/lib/storage-pipes/storage-pipes.module';

@NgModule({
  declarations: [AdministrationContextualMenuComponent, ExecuteCommandMenuItemComponent, ExecuteScriptMenuItemComponent],
  exports: [AdministrationContextualMenuComponent],
  entryComponents: [AdministrationContextualMenuComponent],
  imports: [
    IconModule,
    VendorsModule,
    CommonModule,
    StorageMenuModule,
    StoragePipesModule,
    CommandModule,
  ]
})
export class ContextualMenuModule {
}
