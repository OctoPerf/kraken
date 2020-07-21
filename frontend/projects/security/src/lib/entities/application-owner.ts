import {Owner} from 'projects/security/src/lib/entities/owner';
import {OwnerType} from 'projects/security/src/lib/entities/owner-type';

export class ApplicationOwner implements Owner {
  constructor(
    public applicationId: string,
    public type: OwnerType = 'APPLICATION') {
  }
}
