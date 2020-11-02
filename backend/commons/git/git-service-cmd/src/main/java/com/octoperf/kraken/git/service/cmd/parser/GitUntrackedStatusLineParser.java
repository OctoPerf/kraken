package com.octoperf.kraken.git.service.cmd.parser;

import com.google.common.collect.ImmutableList;
import com.octoperf.kraken.git.entity.GitStatus;
import org.springframework.stereotype.Component;

@Component
final class GitUntrackedStatusLineParser implements GitStatusLineParser {

  @Override
  public Character getChar() {
    return '?';
  }

  @Override
  public GitStatus apply(final GitStatus status, final String[] line) {
    final var builder = new ImmutableList.Builder<String>();
    builder.addAll(status.getUntracked());
    builder.add(line[1]);
    return status.toBuilder()
        .untracked(builder.build())
        .build();
  }
}
