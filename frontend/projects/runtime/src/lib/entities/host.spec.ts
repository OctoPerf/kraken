import {Host} from 'projects/runtime/src/lib/entities/host';
import {testPublicOwner, testUserOwner} from 'projects/security/src/lib/entities/owner.spec';

export const testHost: () => Host = () => {
  return {
    id: 'id0',
    name: 'name0',
    capacity: {cpu: '10', memory: '10GB'},
    allocatable: {cpu: '5', memory: '5GB'},
    addresses: [{type: 'type', address: 'address'}],
    owner: testPublicOwner()
  };
};

export const testHosts: () => Host[] = () => {
  return [
    testHost(),
    {
      id: 'id1',
      name: 'name1',
      capacity: {cpu: '-', memory: '-'},
      allocatable: {cpu: '-', memory: '-'},
      addresses: [],
      owner: testUserOwner()
    },
  ];
};
