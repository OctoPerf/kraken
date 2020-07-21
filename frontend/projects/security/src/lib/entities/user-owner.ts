import {Owner} from 'projects/security/src/lib/entities/owner';
import {OwnerType} from 'projects/security/src/lib/entities/owner-type';
import {KrakenRole} from 'projects/security/src/lib/entities/kraken-role';

export class UserOwner implements Owner {
  constructor(
    public applicationId: string,
    public userId: string,
    public roles: KrakenRole[] = [],
    public type: OwnerType = 'USER') {
  }
}
