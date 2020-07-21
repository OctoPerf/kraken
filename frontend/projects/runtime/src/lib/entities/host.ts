import {HostAddress} from 'projects/runtime/src/lib/entities/host-address';
import {HostCapacity} from 'projects/runtime/src/lib/entities/host-capacity';
import {Owner} from 'projects/security/src/lib/entities/owner';

export interface Host {
  id: string;
  readonly name: string;
  readonly capacity: HostCapacity;
  readonly allocatable: HostCapacity;
  readonly addresses: HostAddress[];
  owner: Owner;
}
