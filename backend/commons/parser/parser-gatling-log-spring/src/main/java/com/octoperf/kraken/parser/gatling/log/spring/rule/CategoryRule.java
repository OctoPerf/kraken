package com.octoperf.kraken.parser.gatling.log.spring.rule;

import com.octoperf.kraken.analysis.entity.DebugEntry;
import com.octoperf.kraken.parser.gatling.log.spring.context.LogParserState;
import com.octoperf.kraken.parser.gatling.log.spring.context.ParserContext;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;

import java.util.Optional;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
abstract class CategoryRule implements ParserRule {

  @NonNull
  ParserContext parserContext;
  @NonNull
  LogParserState outState;
  @NonNull
  String line;

  @Override
  public int order() {
    return 1;
  }

  @Override
  public Optional<DebugEntry> apply(final String line) {
    parserContext.setState(outState);
    return Optional.empty();
  }

  @Override
  public boolean test(final String line) {
    return parserContext.getState() == LogParserState.AFTER_DELIMITER && this.line.equals(line.trim());
  }

}
