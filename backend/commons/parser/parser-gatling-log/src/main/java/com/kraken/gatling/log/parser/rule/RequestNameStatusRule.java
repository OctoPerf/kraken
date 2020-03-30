package com.kraken.gatling.log.parser.rule;

import com.kraken.analysis.entity.DebugEntry;
import com.kraken.gatling.log.parser.context.ParserContext;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.kraken.gatling.log.parser.context.LogParserState.REQUEST;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
final class RequestNameStatusRule implements ContentParserRule {

  @NonNull
  ParserContext parserContext;
  
  @Override
  public Optional<DebugEntry> apply(final String line) {
    parserContext.setRequestNameStatus(line);
    return Optional.empty();
  }

  @Override
  public boolean test(final String line) {
    return parserContext.getState() == REQUEST;
  }

}
