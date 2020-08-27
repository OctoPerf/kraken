import {Project} from 'projects/project/src/app/project/entities/project';

export const testProject: () => Project = () => {
  return {
    id: 'id0',
    name: 'name0',
    applicationId: 'gatling',
    createDate: 42,
    updateDate: 1337,
  };
};

export const testProjects: () => Project[] = () => {
  return [
    testProject(),
    {
      id: 'id1',
      name: 'name1',
      applicationId: 'gatling',
      createDate: 42,
      updateDate: 1337,
    },
  ];
};
