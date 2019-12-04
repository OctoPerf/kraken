import {NgModule} from '@angular/core';
import {HostsTableComponent} from 'projects/runtime/src/lib/runtime-host/hosts-table/hosts-table.component';
import {ComponentsModule} from 'projects/components/src/lib/components.module';
import {VendorsModule} from 'projects/vendors/src/lib/vendors.module';
import {CapacityToStringPipe} from './capacity-to-string.pipe';
import {AddressesToStringPipe} from './addresses-to-string.pipe';
import {IconModule} from 'projects/icon/src/lib/icon.module';
import {RuntimeHostService} from 'projects/runtime/src/lib/runtime-host/runtime-host.service';
import {HostIdToNamePipe} from './host-id-to-name.pipe';
import {HostIdToDescriptionPipe} from './host-id-to-description.pipe';
import {RuntimeWatcherService} from 'projects/runtime/src/lib/runtime-watcher/runtime-watcher.service';

@NgModule({
  imports: [
    ComponentsModule,
    VendorsModule,
    IconModule,
  ],
  declarations: [
    HostsTableComponent,
    CapacityToStringPipe,
    AddressesToStringPipe,
    HostIdToNamePipe,
    HostIdToDescriptionPipe,
  ],
  exports: [
    HostsTableComponent,
    HostIdToNamePipe,
    HostIdToDescriptionPipe,
  ],
  entryComponents: [
    HostsTableComponent,
  ],
  providers: [
    RuntimeHostService,
    CapacityToStringPipe,
    AddressesToStringPipe,
    HostIdToNamePipe,
    HostIdToDescriptionPipe,
  ]
})
export class RuntimeHostModule {
}
