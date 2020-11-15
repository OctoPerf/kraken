export interface GitBranchStatus {

  oid: string;
  head: string;
  upstream: string;
  ahead: number;
  behind: number;

}
