import {Pipe, PipeTransform} from '@angular/core';
import {RuntimeHostService} from 'projects/runtime/src/lib/runtime-host/runtime-host.service';
import {Host} from 'projects/runtime/src/lib/entities/host';

@Pipe({
  name: 'hostIdToHost'
})
export class HostIdToHostPipe implements PipeTransform {

  constructor(private hostService: RuntimeHostService) {
  }

  transform(id: string): Host {
    return this.hostService.host(id);
  }

}
