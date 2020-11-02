package com.octoperf.kraken.git.service.cmd.parser;

import com.octoperf.kraken.git.entity.GitStatus;
import org.springframework.stereotype.Component;

@Component
final class GitBranchStatusLineParser implements GitStatusLineParser {

  @Override
  public Character getChar() {
    return '#';
  }

  @Override
  public GitStatus apply(final GitStatus status, final String[] line) {
    final var type = line[1];
    switch (type) {
      case "branch.oid":
        return status.toBuilder().branch(status.getBranch().toBuilder().oid(line[2]).build()).build();
      case "branch.head":
        return status.toBuilder().branch(status.getBranch().toBuilder().head(line[2]).build()).build();
      case "branch.upstream":
        return status.toBuilder().branch(status.getBranch().toBuilder().upstream(line[2]).build()).build();
      case "branch.ab":
        final var ahead = Long.parseLong(line[2].substring(1));
        final var behind = Long.parseLong(line[3].substring(1));
        return status.toBuilder().branch(status.getBranch().toBuilder().ahead(ahead).behind(behind).build()).build();
      default:
        return status;
    }
  }
}
