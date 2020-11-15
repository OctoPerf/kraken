import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {VendorsModule} from 'projects/vendors/src/lib/vendors.module';
import { SshPublicKeyComponent } from './ssh-public-key/ssh-public-key.component';
import {IconModule} from 'projects/icon/src/lib/icon.module';
import {ComponentsModule} from 'projects/components/src/lib/components.module';
import {HelpModule} from 'projects/help/src/lib/help.module';

@NgModule({
  declarations: [SshPublicKeyComponent],
  imports: [
    CommonModule,
    VendorsModule,
    IconModule,
    ComponentsModule,
  ],
  exports: [SshPublicKeyComponent]
})
export class GitUserModule { }
