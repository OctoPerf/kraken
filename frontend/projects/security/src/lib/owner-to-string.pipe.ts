import {Pipe, PipeTransform} from '@angular/core';
import {Owner} from 'projects/security/src/lib/entities/owner';
import {OwnerType} from 'projects/security/src/lib/entities/owner-type';

@Pipe({
  name: 'ownerToString'
})
export class OwnerToStringPipe implements PipeTransform {

  private static ownerMap: Map<OwnerType, (owner: Owner) => string> = new Map([
    ['PUBLIC', (owner: Owner) => 'Public'],
    ['USER', (owner: Owner) => `User ${owner.userId} on ${owner.applicationId}`],
    ['APPLICATION', (owner: Owner) => `Application ${owner.applicationId}`],
  ]);

  transform(owner: Owner): string {
    return OwnerToStringPipe.ownerMap.get(owner.type)(owner);
  }

}
