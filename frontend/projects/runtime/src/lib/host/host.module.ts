import {NgModule} from '@angular/core';
import {HostsTableComponent} from 'projects/runtime/src/lib/host/hosts-table/hosts-table.component';
import {ComponentsModule} from 'projects/components/src/lib/components.module';
import {VendorsModule} from 'projects/vendors/src/lib/vendors.module';
import {CapacityToStringPipe} from './capacity-to-string.pipe';
import {AddressesToStringPipe} from './addresses-to-string.pipe';
import {IconModule} from 'projects/icon/src/lib/icon.module';
import {HostService} from 'projects/runtime/src/lib/host/host.service';

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
  ],
  exports: [
    HostsTableComponent,
  ],
  entryComponents: [
    HostsTableComponent,
  ],
  providers: [
    HostService,
  ]
})
export class HostModule {
}
