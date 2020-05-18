import {OwnerToStringPipe} from './owner-to-string.pipe';
import {testApplicationOwner, testPublicOwner, testUserOwner} from 'projects/security/src/lib/entities/owner.spec';

describe('OwnerToStringPipe', () => {

  let pipe: OwnerToStringPipe;

  beforeEach(() => {
    pipe = new OwnerToStringPipe();
  });

  it('create an instance', () => {
    expect(pipe).toBeTruthy();
  });

  it('create transform public', () => {
    expect(pipe.transform(testPublicOwner()))
      .toEqual('Public');
  });

  it('create transform user', () => {
    expect(pipe.transform(testUserOwner()))
      .toEqual('User userId on gatling');
  });

  it('create transform application', () => {
    expect(pipe.transform(testApplicationOwner()))
      .toEqual('Application gatling');
  });
});
