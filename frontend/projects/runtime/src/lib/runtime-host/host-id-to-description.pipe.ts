import {Pipe, PipeTransform} from '@angular/core';
import {RuntimeHostService} from 'projects/runtime/src/lib/runtime-host/runtime-host.service';
import {AddressesToStringPipe} from 'projects/runtime/src/lib/runtime-host/addresses-to-string.pipe';
import {CapacityToStringPipe} from 'projects/runtime/src/lib/runtime-host/capacity-to-string.pipe';

@Pipe({
  name: 'hostIdToDescription'
})
export class HostIdToDescriptionPipe implements PipeTransform {

  constructor(private hostService: RuntimeHostService,
              private addressesToStringPipe: AddressesToStringPipe,
              private capacityToStringPipe: CapacityToStringPipe) {
  }

  transform(id: string): string {
    const host = this.hostService.host(id);
    return host ? `Addresses: ${this.addressesToStringPipe.transform(host.addresses)} - Capacity: ${this.capacityToStringPipe.transform(host.capacity)}` : id;
  }

}
