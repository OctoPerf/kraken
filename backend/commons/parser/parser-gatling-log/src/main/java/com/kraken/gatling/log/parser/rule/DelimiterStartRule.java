package com.kraken.gatling.log.parser.rule;

import com.kraken.analysis.entity.DebugEntry;
import com.kraken.gatling.log.parser.context.LogParserState;
import com.kraken.gatling.log.parser.context.ParserContext;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.kraken.gatling.log.parser.context.LogParserState.VOID;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
final class DelimiterStartRule implements ParserRule {

  final static String LINE = ">>>>>>>>>>>>>>>>>>>>>>>>>>";

  @NonNull
  ParserContext parserContext;

  @Override
  public Optional<DebugEntry> apply(final String line) {
    parserContext.reset();
    parserContext.setState(LogParserState.AFTER_DELIMITER);
    return Optional.empty();
  }

  @Override
  public boolean test(final String line) {
    return parserContext.getState() == VOID && LINE.equals(line);
  }

}
