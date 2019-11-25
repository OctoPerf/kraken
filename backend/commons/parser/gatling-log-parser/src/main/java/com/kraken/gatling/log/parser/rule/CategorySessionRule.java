package com.kraken.gatling.log.parser.rule;

import com.kraken.gatling.log.parser.context.ParserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.kraken.gatling.log.parser.context.LogParserState.SESSION;

@Component
final class CategorySessionRule extends CategoryRule {

  @Autowired
  public CategorySessionRule(final ParserContext parserContext) {
    super(parserContext, SESSION, "Session:");
  }
}
