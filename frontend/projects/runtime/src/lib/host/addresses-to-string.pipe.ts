import {Pipe, PipeTransform} from '@angular/core';
import {HostAddress} from 'projects/runtime/src/lib/entities/host-address';

@Pipe({
  name: 'addressesToString'
})
export class AddressesToStringPipe implements PipeTransform {

  transform(addresses: HostAddress[]): string {
    return addresses.map((address: HostAddress) => `${address.type}=${address.address}`).join(', ');
  }

}
