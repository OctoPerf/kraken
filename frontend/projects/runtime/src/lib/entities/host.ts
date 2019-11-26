import {HostAddress} from 'projects/runtime/src/lib/entities/host-address';

export interface Host {
  readonly id: string;
  readonly name: string;
  readonly capacity: {[key in string]: string};
  readonly addresses: HostAddress[];
}
