package com.octoperf.kraken.parser.gatling.log.spring.rule;

import com.octoperf.kraken.parser.gatling.log.spring.context.LogParserState;
import com.octoperf.kraken.parser.gatling.log.spring.context.LogParserSubState;
import com.octoperf.kraken.parser.gatling.log.spring.context.ParserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

@Component
final class SubCategoryHeadersRule extends SubCategoryRule {

  @Autowired
  public SubCategoryHeadersRule(final ParserContext parserContext) {
    super(parserContext, LogParserSubState.HEADERS, asList(LogParserState.HTTP_RESPONSE, LogParserState.HTTP_REQUEST), singletonList("headers="));
  }

}
