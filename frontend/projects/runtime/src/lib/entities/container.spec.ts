import {Container} from 'projects/runtime/src/lib/entities/container';

export const testContainer: () => Container = () => {
  return {
    id: 'id',
    name: 'name',
    hostId: 'hostId',
    label: 'label',
    startDate: 0,
    status: 'READY'
  };
};

export const testContainers: () => Container[] = () => {
  return [
    {
      id: 'id1',
      name: 'name1',
      hostId: 'hostId1',
      label: 'label1',
      startDate: 1,
      status: 'READY'
    },
    {
      id: 'id2',
      name: 'name2',
      hostId: 'hostId2',
      label: 'label2',
      startDate: 2,
      status: 'READY'
    }
  ];
};
