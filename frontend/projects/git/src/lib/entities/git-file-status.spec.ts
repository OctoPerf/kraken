import {GitFileStatus} from 'projects/git/src/lib/entities/git-file-status';

export const testGitFileStatus: () => GitFileStatus = () => {
  return {
    xy: 'AM',
    path: 'path'
  };
};
