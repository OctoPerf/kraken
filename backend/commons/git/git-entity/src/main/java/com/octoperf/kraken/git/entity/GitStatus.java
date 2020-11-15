package com.octoperf.kraken.git.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.util.List;

@Value
public class GitStatus {

  GitBranchStatus branch;
  List<GitFileStatus> changed;
  List<GitRenamedCopiedStatus> renamedCopied;
  List<GitFileStatus> unmerged;
  List<String> untracked;
  List<String> ignored;

  @JsonCreator
  @Builder(toBuilder = true)
  public GitStatus(@NonNull @JsonProperty("branch") final GitBranchStatus branch,
                   @NonNull @JsonProperty("changed") final List<GitFileStatus> changed,
                   @NonNull @JsonProperty("renamedCopied") final List<GitRenamedCopiedStatus> renamedCopied,
                   @NonNull @JsonProperty("unmerged") final List<GitFileStatus> unmerged,
                   @NonNull @JsonProperty("untracked") final List<String> untracked,
                   @NonNull @JsonProperty("ignored") final List<String> ignored) {
    this.branch = branch;
    this.changed = changed;
    this.renamedCopied = renamedCopied;
    this.unmerged = unmerged;
    this.untracked = untracked;
    this.ignored = ignored;
  }
}
