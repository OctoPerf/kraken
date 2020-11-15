package com.octoperf.kraken.git.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

/**
 * <pre>
 *   Line                                     Notes
 * ------------------------------------------------------------
 * # branch.oid <commit> | (initial)        Current commit.
 * # branch.head <branch> | (detached)      Current branch.
 * # branch.upstream <upstream_branch>      If upstream is set.
 * # branch.ab +<ahead> -<behind>           If upstream is set and
 * 					 the commit is present.
 * ------------------------------------------------------------
 * </pre>
 */
@Value
public class GitBranchStatus {
  // # branch.oid <commit> | (initial)        Current commit.
  String oid;
  // # branch.head <branch> | (detached)      Current branch.
  String head;
  // # branch.upstream <upstream_branch>      If upstream is set.
  String upstream;
  // # branch.ab +<ahead> -<behind>           If upstream is set and the commit is present.
  Long ahead;
  Long behind;

  @JsonCreator
  @Builder(toBuilder = true)
  public GitBranchStatus(@JsonProperty("oid") final String oid,
                         @JsonProperty("head") final String head,
                         @JsonProperty("upstream") final String upstream,
                         @JsonProperty("ahead") final Long ahead,
                         @JsonProperty("behind") final Long behind) {
    this.oid = oid;
    this.head = head;
    this.upstream = upstream;
    this.ahead = ahead;
    this.behind = behind;
  }
}
