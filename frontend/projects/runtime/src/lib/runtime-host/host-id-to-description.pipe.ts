import {Pipe, PipeTransform} from '@angular/core';
import {RuntimeHostService} from 'projects/runtime/src/lib/runtime-host/runtime-host.service';
import {AddressesToStringPipe} from 'projects/runtime/src/lib/runtime-host/addresses-to-string.pipe';

@Pipe({
  name: 'hostIdToDescription'
})
export class HostIdToDescriptionPipe implements PipeTransform {

  constructor(private hostService: RuntimeHostService,
              private addressesToStringPipe: AddressesToStringPipe) {
  }

  transform(id: string): string {
    const host = this.hostService.host(id);
    return host ? `Name: ${host.name} - CPU: ${host.allocatable.cpu} / ${host.capacity.cpu} - Memory: ${host.allocatable.memory} / ${host.capacity.memory} - Addresses: ${this.addressesToStringPipe.transform(host.addresses)}` : id;
  }

}
