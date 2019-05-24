import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {ToggleButtonComponent} from './toggle-button/toggle-button.component';
import {VendorsModule} from 'projects/vendors/src/lib/vendors.module';
import { ContextualMenuComponent } from './contextual-menu/contextual-menu.component';
import {IconModule} from 'projects/icon/src/lib/icon.module';

@NgModule({
  imports: [
    CommonModule,
    VendorsModule,
    IconModule,
  ],
  declarations: [
    ToggleButtonComponent,
    ContextualMenuComponent,
  ],
  exports: [
    ToggleButtonComponent,
    ContextualMenuComponent,
  ]
})
export class TreeModule {
}
