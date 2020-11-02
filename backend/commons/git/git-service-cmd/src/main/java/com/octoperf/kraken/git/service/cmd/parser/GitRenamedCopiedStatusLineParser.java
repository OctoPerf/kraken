package com.octoperf.kraken.git.service.cmd.parser;

import com.google.common.collect.ImmutableList;
import com.octoperf.kraken.git.entity.GitRenamedCopiedStatus;
import com.octoperf.kraken.git.entity.GitStatus;
import org.springframework.stereotype.Component;

@Component
final class GitRenamedCopiedStatusLineParser implements GitStatusLineParser {

  @Override
  public Character getChar() {
    return '2';
  }

  @Override
  public GitStatus apply(final GitStatus status, final String[] line) {
    final var length = line.length;
    final var builder = new ImmutableList.Builder<GitRenamedCopiedStatus>();
    builder.addAll(status.getRenamedCopied());
    builder.add(GitRenamedCopiedStatus.builder()
        .xy(line[1])
        .origPath(line[length - 1])
        .path(line[length - 2])
        .score(line[length - 3])
        .build());
    return status.toBuilder()
        .renamedCopied(builder.build())
        .build();
  }
}
