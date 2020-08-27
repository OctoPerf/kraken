import {Owner} from 'projects/security/src/lib/entities/owner';

export const testPublicOwner: () => Owner = () => {
  return new Owner();
};

export const testUserOwner: () => Owner = () => {
  return new Owner('userId', 'projectId', 'gatling', 'USER');
};

export const testApplicationOwner: () => Owner = () => {
  return new Owner('', '', 'gatling', 'APPLICATION');
};
