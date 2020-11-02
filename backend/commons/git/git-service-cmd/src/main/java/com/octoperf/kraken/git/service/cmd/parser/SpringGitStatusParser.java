package com.octoperf.kraken.git.service.cmd.parser;

import com.octoperf.kraken.git.entity.GitBranchStatus;
import com.octoperf.kraken.git.entity.GitStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

import static com.google.common.collect.ImmutableList.of;

@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
final class SpringGitStatusParser implements GitStatusParser {

  @NonNull Map<Character, GitStatusLineParser> parsers;

  @Override
  public Mono<GitStatus> apply(Flux<String> stringFlux) {
    final var status = GitStatus.builder()
        .branch(GitBranchStatus.builder().build())
        .untracked(of())
        .ignored(of())
        .renamedCopied(of())
        .unmerged(of())
        .changed(of())
        .build();
    return stringFlux.map(line -> line.split("\\s+"))
        .reduce(status, (current, strings) -> parsers.get(strings[0].charAt(0)).apply(current, strings));
  }
}
