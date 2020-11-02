import {GitStatus} from 'projects/git/src/lib/entities/git-status';

export const testGitStatus: () => GitStatus = () => {
  return {
    branch: {
      oid: 'oid',
      head: 'head',
      upstream: 'upstream',
      ahead: 42,
      behind: 1337,
    },
    changed: [{
      xy: 'xy',
      path: 'path',
    }],
    renamedCopied: [{
      xy: 'xy',
      score: 'score',
      path: 'path',
      origPath: 'origPath',
    }],
    unmerged: [{
      xy: 'xy',
      path: 'path',
    }],
    untracked: ['untracked'],
    ignored: ['ignored'],
  };
};
