import {NgModule} from '@angular/core';
import {HostsTableComponent} from 'projects/runtime/src/lib/runtime-host/hosts-table/hosts-table.component';
import {ComponentsModule} from 'projects/components/src/lib/components.module';
import {VendorsModule} from 'projects/vendors/src/lib/vendors.module';
import {AddressesToStringPipe} from './addresses-to-string.pipe';
import {IconModule} from 'projects/icon/src/lib/icon.module';
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
    AddressesToStringPipe,
    HostIdToDescriptionPipe,
    EnvironmentVariablesListComponent,
  ],
  exports: [
    HostsTableComponent,
    HostsSelectorComponent,
    HostIdToDescriptionPipe,
    EnvironmentVariablesListComponent,
  ],
  entryComponents: [
    HostsTableComponent,
  ],
  providers: [
    PrettyStringPipe,
    AddressesToStringPipe,
    HostIdToDescriptionPipe,
  ]
})
export class RuntimeHostModule {
}
