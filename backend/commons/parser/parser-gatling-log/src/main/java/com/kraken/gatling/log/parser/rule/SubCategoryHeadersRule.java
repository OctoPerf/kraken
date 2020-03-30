package com.kraken.gatling.log.parser.rule;

import com.kraken.gatling.log.parser.context.ParserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.kraken.gatling.log.parser.context.LogParserState.HTTP_REQUEST;
import static com.kraken.gatling.log.parser.context.LogParserState.HTTP_RESPONSE;
import static com.kraken.gatling.log.parser.context.LogParserSubState.HEADERS;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

@Component
final class SubCategoryHeadersRule extends SubCategoryRule {

  @Autowired
  public SubCategoryHeadersRule(final ParserContext parserContext) {
    super(parserContext, HEADERS, asList(HTTP_RESPONSE, HTTP_REQUEST), singletonList("headers="));
  }

}
