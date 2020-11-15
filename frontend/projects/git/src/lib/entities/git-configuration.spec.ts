import {GitConfiguration} from 'projects/git/src/lib/entities/git-configuration';

export const testGitConfiguration: () => GitConfiguration = () => {
  return {
    repositoryUrl: 'repositoryUrl'
  };
};
