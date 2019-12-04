import {NgModule} from '@angular/core';
import {HostsTableComponent} from 'projects/runtime/src/lib/runtime-host/hosts-table/hosts-table.component';
import {ComponentsModule} from 'projects/components/src/lib/components.module';
import {VendorsModule} from 'projects/vendors/src/lib/vendors.module';
import {CapacityToStringPipe} from './capacity-to-string.pipe';
import {AddressesToStringPipe} from './addresses-to-string.pipe';
import {IconModule} from 'projects/icon/src/lib/icon.module';
import {HostIdToNamePipe} from './host-id-to-name.pipe';
import {HostIdToDescriptionPipe} from './host-id-to-description.pipe';
import {PrettyStringPipe} from 'projects/tools/src/lib/pretty-string.pipe';
import {HostsSelectorComponent} from 'projects/runtime/src/lib/runtime-host/hosts-selector/hosts-selector.component';
import {CommonModule} from '@angular/common';
import {EnvironmentVariablesListComponent} from './environment-variables-list/environment-variables-list.component';

@NgModule({
  imports: [
    CommonModule,
    ComponentsModule,
    VendorsModule,
    IconModule,
  ],
  declarations: [
    HostsSelectorComponent,
    HostsTableComponent,
    CapacityToStringPipe,
    AddressesToStringPipe,
    HostIdToNamePipe,
    HostIdToDescriptionPipe,
    EnvironmentVariablesListComponent,
  ],
  exports: [
    HostsTableComponent,
    HostsSelectorComponent,
    HostIdToNamePipe,
    HostIdToDescriptionPipe,
    EnvironmentVariablesListComponent,
  ],
  entryComponents: [
    HostsTableComponent,
  ],
  providers: [
    PrettyStringPipe,
    CapacityToStringPipe,
    AddressesToStringPipe,
    HostIdToNamePipe,
    HostIdToDescriptionPipe,
  ]
})
export class RuntimeHostModule {
}
