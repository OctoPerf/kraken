package com.octoperf.kraken.parser.gatling.log.spring.rule;

import com.octoperf.kraken.analysis.entity.DebugEntry;
import com.octoperf.kraken.parser.gatling.log.spring.context.LogParserState;
import com.octoperf.kraken.parser.gatling.log.spring.context.LogParserSubState;
import com.octoperf.kraken.parser.gatling.log.spring.context.ParserContext;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Optional;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
abstract class SubCategoryRule implements ParserRule {

  @NonNull
  ParserContext parserContext;
  @NonNull
  LogParserSubState outState;
  @NonNull
  List<LogParserState> states;
  @NonNull
  List<String> lines;

  @Override
  public int order() {
    return 2;
  }

  @Override
  public Optional<DebugEntry> apply(final String line) {
    parserContext.setSubState(outState);
    return Optional.empty();
  }

  @Override
  public boolean test(final String line) {
    return this.states.contains(parserContext.getState()) && this.lines.contains(line.trim());
  }

}
