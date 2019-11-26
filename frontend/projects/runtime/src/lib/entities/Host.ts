import {HostAddress} from 'projects/runtime/src/lib/entities/HostAddress';

export interface Host {
  readonly id: string;
  readonly name: string;
  readonly capacity: {[key in string]: string};
  readonly addresses: HostAddress[];
}
