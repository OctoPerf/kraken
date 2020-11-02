import {GitLog} from 'projects/git/src/lib/entities/git-log';
import {testUserOwner} from 'projects/security/src/lib/entities/owner.spec';

export const testGitLog: () => GitLog = () => {
  return {owner: testUserOwner(), text: 'text'};
};
