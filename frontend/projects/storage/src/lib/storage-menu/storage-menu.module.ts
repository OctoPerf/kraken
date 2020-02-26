import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {StorageContextualMenuComponent} from './storage-contextual-menu/storage-contextual-menu.component';
import {IconModule} from 'projects/icon/src/lib/icon.module';
import {VendorsModule} from 'projects/vendors/src/lib/vendors.module';
import {TreeModule} from 'projects/tree/src/lib/tree.module';
import {StorageNodeButtonsComponent} from './storage-node-buttons/storage-node-buttons.component';
import {EditNodeButtonComponent} from './buttons/edit-node-button/edit-node-button.component';
import {RenameNodeButtonComponent} from './buttons/rename-node-button/rename-node-button.component';
import {MenuNodeButtonComponent} from './buttons/menu-node-button/menu-node-button.component';
import {NewDirectoryMenuItemComponent} from './menu-items/new-directory-menu-item/new-directory-menu-item.component';
import {NewFileMenuItemComponent} from './menu-items/new-file-menu-item/new-file-menu-item.component';
import {UploadMenuItemComponent} from './menu-items/upload-menu-item/upload-menu-item.component';
import {DownloadMenuItemComponent} from './menu-items/download-menu-item/download-menu-item.component';
import {CopyMenuItemComponent} from './menu-items/copy-menu-item/copy-menu-item.component';
import {CutMenuItemComponent} from './menu-items/cut-menu-item/cut-menu-item.component';
import {PasteMenuItemComponent} from './menu-items/paste-menu-item/paste-menu-item.component';
import {DeleteMenuItemComponent} from './menu-items/delete-menu-item/delete-menu-item.component';
import { RenameMenuItemComponent } from './menu-items/rename-menu-item/rename-menu-item.component';

@NgModule({
  declarations: [
    StorageContextualMenuComponent,
    StorageNodeButtonsComponent,
    EditNodeButtonComponent,
    RenameNodeButtonComponent,
    MenuNodeButtonComponent,
    NewDirectoryMenuItemComponent,
    NewFileMenuItemComponent,
    UploadMenuItemComponent,
    DownloadMenuItemComponent,
    CopyMenuItemComponent,
    CutMenuItemComponent,
    PasteMenuItemComponent,
    DeleteMenuItemComponent,
    RenameMenuItemComponent,
  ],
  exports: [
    StorageContextualMenuComponent,
    StorageNodeButtonsComponent,
    EditNodeButtonComponent,
    RenameNodeButtonComponent,
    MenuNodeButtonComponent,
  ],
  imports: [
    CommonModule,
    VendorsModule,
    IconModule,
    TreeModule,
  ]
})
export class StorageMenuModule {
}
