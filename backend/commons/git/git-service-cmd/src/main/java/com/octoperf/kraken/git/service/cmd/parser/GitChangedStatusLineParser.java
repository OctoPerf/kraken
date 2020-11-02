package com.octoperf.kraken.git.service.cmd.parser;

import com.google.common.collect.ImmutableList;
import com.octoperf.kraken.git.entity.GitFileStatus;
import com.octoperf.kraken.git.entity.GitStatus;
import org.springframework.stereotype.Component;

@Component
final class GitChangedStatusLineParser implements GitStatusLineParser {

  @Override
  public Character getChar() {
    return '1';
  }

  @Override
  public GitStatus apply(final GitStatus status, final String[] line) {
    final var builder = new ImmutableList.Builder<GitFileStatus>();
    builder.addAll(status.getChanged());
    builder.add(GitFileStatus.builder()
        .xy(line[1])
        .path(line[line.length - 1])
        .build());
    return status.toBuilder()
        .changed(builder.build())
        .build();
  }
}
