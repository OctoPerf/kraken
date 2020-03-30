package com.kraken.gatling.log.parser.rule;

import com.kraken.analysis.entity.DebugEntry;
import com.kraken.gatling.log.parser.context.ParserContext;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.kraken.gatling.log.parser.context.LogParserState.HTTP_RESPONSE;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
final class DelimiterEndRule implements ParserRule {

  final static String LINE = "<<<<<<<<<<<<<<<<<<<<<<<<<";

  @NonNull
  ParserContext parserContext;

  @Override
  public Optional<DebugEntry> apply(final String line) {
    return parserContext.delimiterEnd();
  }

  @Override
  public boolean test(final String line) {
    return parserContext.getState() == HTTP_RESPONSE && LINE.equals(line);
  }

}
