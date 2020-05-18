import {Owner} from 'projects/security/src/lib/entities/owner';
import {OwnerType} from 'projects/security/src/lib/entities/owner-type';

export class PublicOwner implements Owner {
  constructor(
    public type: OwnerType = 'PUBLIC') {
  }
}
