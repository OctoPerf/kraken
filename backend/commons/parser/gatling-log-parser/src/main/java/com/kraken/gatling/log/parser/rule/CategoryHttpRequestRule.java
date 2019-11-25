package com.kraken.gatling.log.parser.rule;

import com.kraken.gatling.log.parser.context.ParserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.kraken.gatling.log.parser.context.LogParserState.HTTP_REQUEST;

@Component
final class CategoryHttpRequestRule extends CategoryRule {

  @Autowired
  public CategoryHttpRequestRule(final ParserContext parserContext) {
    super(parserContext, HTTP_REQUEST, "HTTP request:");
  }

}
