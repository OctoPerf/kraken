import {Host} from 'projects/runtime/src/lib/entities/host';

export const testHost: () => Host = () => {
  return {id: 'id0', name: 'name0', capacity: {'key': 'value'}, addresses: [{type: 'type', address: 'address'}]};
};

export const testHosts: () => Host[] = () => {
  return [
    testHost(),
    {id: 'id1', name: 'name1', capacity: {}, addresses: []},
  ];
};
