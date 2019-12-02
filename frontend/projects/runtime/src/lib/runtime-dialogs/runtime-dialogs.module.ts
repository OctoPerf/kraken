import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HostsSelectorComponent } from './hosts-selector/hosts-selector.component';
import {VendorsModule} from 'projects/vendors/src/lib/vendors.module';



@NgModule({
  declarations: [HostsSelectorComponent],
  imports: [
    CommonModule,
    VendorsModule
  ]
})
export class RuntimeDialogsModule { }
