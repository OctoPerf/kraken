import {Pipe, PipeTransform} from '@angular/core';
import {ContainerStatus} from 'projects/runtime/src/lib/entities/container-status';

@Pipe({
  name: 'containerStatusIsTerminal'
})
export class ContainerStatusIsTerminalPipe implements PipeTransform {

  transform(status: ContainerStatus): boolean {
    return (status === 'DONE' || status === 'FAILED');
  }

}
