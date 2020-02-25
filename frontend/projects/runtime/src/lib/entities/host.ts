import {HostAddress} from 'projects/runtime/src/lib/entities/host-address';
import {HostCapacity} from 'projects/runtime/src/lib/entities/host-capacity';

export interface Host {
  readonly id: string;
  readonly name: string;
  readonly capacity: HostCapacity;
  readonly allocatable: HostCapacity;
  readonly addresses: HostAddress[];
}
