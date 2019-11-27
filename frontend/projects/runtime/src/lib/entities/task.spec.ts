import {Task} from 'projects/runtime/src/lib/entities/task';
import {testContainers} from 'projects/runtime/src/lib/entities/container.spec';

export const testTask: () => Task = () => {
  return {
    id: 'id',
    startDate: 0,
    status: 'READY',
    type: 'RUN',
    containers: testContainers(),
    expectedCount: 2,
    description: 'description'
  };
};


export const testTasks: () => Task[] = () => {
  return [
    {
      id: 'id1',
      startDate: 0,
      status: 'READY',
      type: 'RUN',
      containers: testContainers(),
      expectedCount: 2,
      description: 'description1'
    },
    {
      id: 'id2',
      startDate: 0,
      status: 'READY',
      type: 'RUN',
      containers: testContainers(),
      expectedCount: 2,
      description: 'description2'
    }
  ];
};
