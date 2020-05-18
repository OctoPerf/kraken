package com.kraken.gatling.log.parser;

import com.kraken.analysis.entity.DebugEntry;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.nio.file.Path;
import java.util.Optional;
import java.util.function.Function;

@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
final class GatlingLogParser implements LogParser {

  @NonNull
  Function<Path, Flux<String>> pathToLines;

  @NonNull
  RulesApplier rulesApplier;

  public Flux<DebugEntry> parse(final Path logFilePath) {
    final var linesFlux = pathToLines.apply(logFilePath);
    return linesFlux.map(rulesApplier).filter(Optional::isPresent).map(Optional::get);
  }

}
