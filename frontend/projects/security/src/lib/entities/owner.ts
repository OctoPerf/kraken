import {OwnerType} from 'projects/security/src/lib/entities/owner-type';
import {KrakenRole} from 'projects/security/src/lib/entities/kraken-role';

export class Owner {
  constructor(
    public userId= '',
    public projectId = '',
    public applicationId = '',
    public type: OwnerType = 'PUBLIC',
    public roles: KrakenRole[] = []) {
  }
}
