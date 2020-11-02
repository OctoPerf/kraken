import {GitRenamedCopiedStatus} from 'projects/git/src/lib/entities/git-renamed-copied-status';

export const testRenamedCopiedStatus: () => GitRenamedCopiedStatus = () => {
  return {
    xy: 'AM',
    score: 'score',
    origPath: 'origPath',
    path: 'path'
  };
};
