import {Pipe, PipeTransform} from '@angular/core';
import {RuntimeHostService} from 'projects/runtime/src/lib/runtime-host/runtime-host.service';

@Pipe({
  name: 'hostIdToName'
})
export class HostIdToNamePipe implements PipeTransform {

  constructor(private hostService: RuntimeHostService) {
  }

  transform(id: string): string {
    const host = this.hostService.host(id);
    return host ? host.name : id;
  }

}
