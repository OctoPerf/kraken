package com.octoperf.kraken.parser.gatling.log.spring.rule;

import com.octoperf.kraken.analysis.entity.DebugEntry;
import com.octoperf.kraken.parser.gatling.log.spring.context.ParserContext;
import com.octoperf.kraken.parser.gatling.log.spring.context.LogParserState;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.util.Optional;

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
    return parserContext.getState() == LogParserState.REQUEST;
  }

}
