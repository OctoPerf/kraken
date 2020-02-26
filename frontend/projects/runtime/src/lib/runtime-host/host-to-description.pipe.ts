import {Pipe, PipeTransform} from '@angular/core';
import {AddressesToStringPipe} from 'projects/runtime/src/lib/runtime-host/addresses-to-string.pipe';
import {Host} from 'projects/runtime/src/lib/entities/host';

@Pipe({
  name: 'hostToDescription'
})
export class HostToDescriptionPipe implements PipeTransform {

  constructor(private addressesToStringPipe: AddressesToStringPipe) {
  }

  transform(host: Host): string {
    return `Name: ${host.name} - CPU: ${host.allocatable.cpu} / ${host.capacity.cpu} - Memory: ${host.allocatable.memory} / ${host.capacity.memory} - Addresses: ${this.addressesToStringPipe.transform(host.addresses)}`;
  }

}
