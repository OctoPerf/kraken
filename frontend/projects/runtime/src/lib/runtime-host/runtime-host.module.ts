import {NgModule} from '@angular/core';
import {HostsTableComponent} from 'projects/runtime/src/lib/runtime-host/hosts-table/hosts-table.component';
import {ComponentsModule} from 'projects/components/src/lib/components.module';
import {VendorsModule} from 'projects/vendors/src/lib/vendors.module';
import {AddressesToStringPipe} from './addresses-to-string.pipe';
import {IconModule} from 'projects/icon/src/lib/icon.module';
import {HostToDescriptionPipe} from 'projects/runtime/src/lib/runtime-host/host-to-description.pipe';
import {PrettyStringPipe} from 'projects/tools/src/lib/pretty-string.pipe';
import {HostsSelectorComponent} from 'projects/runtime/src/lib/runtime-host/hosts-selector/hosts-selector.component';
import {CommonModule} from '@angular/common';
import {EnvironmentVariablesListComponent} from './environment-variables-list/environment-variables-list.component';
import {HostIdToHostPipe} from 'projects/runtime/src/lib/runtime-host/host-id-to-host.pipe';
import {RuntimeHostDialogsModule} from 'projects/runtime/src/lib/runtime-host/runtime-host-dialogs/runtime-host-dialogs.module';

@NgModule({
  imports: [
    CommonModule,
    ComponentsModule,
    VendorsModule,
    IconModule,
    RuntimeHostDialogsModule,
  ],
  declarations: [
    HostsSelectorComponent,
    HostsTableComponent,
    AddressesToStringPipe,
    HostToDescriptionPipe,
    HostIdToHostPipe,
    EnvironmentVariablesListComponent,
  ],
  exports: [
    HostsTableComponent,
    HostsSelectorComponent,
    HostToDescriptionPipe,
    HostIdToHostPipe,
    EnvironmentVariablesListComponent,
  ],
  entryComponents: [
    HostsTableComponent,
  ],
  providers: [
    PrettyStringPipe,
    AddressesToStringPipe,
    HostToDescriptionPipe,
    HostIdToHostPipe,
  ]
})
export class RuntimeHostModule {
}
