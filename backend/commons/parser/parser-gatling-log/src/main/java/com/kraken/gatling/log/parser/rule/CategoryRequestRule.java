package com.kraken.gatling.log.parser.rule;

import com.kraken.gatling.log.parser.context.ParserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.kraken.gatling.log.parser.context.LogParserState.REQUEST;

@Component
final class CategoryRequestRule extends CategoryRule {

  @Autowired
  public CategoryRequestRule(final ParserContext parserContext) {
    super(parserContext, REQUEST, "Request:");
  }

}
