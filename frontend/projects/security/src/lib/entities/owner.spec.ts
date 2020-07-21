import {PublicOwner} from 'projects/security/src/lib/entities/public-owner';
import {UserOwner} from 'projects/security/src/lib/entities/user-owner';
import {ApplicationOwner} from 'projects/security/src/lib/entities/application-owner';

export const testPublicOwner: () => PublicOwner = () => {
  return new PublicOwner();
};

export const testUserOwner: () => UserOwner = () => {
  return new UserOwner('gatling', 'userId');
};

export const testApplicationOwner: () => ApplicationOwner = () => {
  return new ApplicationOwner('gatling');
};
