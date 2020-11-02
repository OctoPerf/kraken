import {GitBranchStatus} from 'projects/git/src/lib/entities/git-branch-status';
import {GitFileStatus} from 'projects/git/src/lib/entities/git-file-status';
import {GitRenamedCopiedStatus} from 'projects/git/src/lib/entities/git-renamed-copied-status';

export interface GitStatus {

  branch: GitBranchStatus;
  changed: Array<GitFileStatus>;
  renamedCopied: Array<GitRenamedCopiedStatus>;
  unmerged: Array<GitFileStatus>;
  untracked: string[];
  ignored: string[];

}
