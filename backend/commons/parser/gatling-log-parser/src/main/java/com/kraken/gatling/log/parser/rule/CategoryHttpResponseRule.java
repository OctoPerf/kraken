package com.kraken.gatling.log.parser.rule;

import com.kraken.gatling.log.parser.context.ParserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.kraken.gatling.log.parser.context.LogParserState.HTTP_RESPONSE;

@Component
final class CategoryHttpResponseRule extends CategoryRule {

  @Autowired
  public CategoryHttpResponseRule(final ParserContext parserContext) {
    super(parserContext, HTTP_RESPONSE, "HTTP response:");
  }

}
