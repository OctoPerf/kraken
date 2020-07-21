package com.octoperf.kraken.parser.gatling.log.spring.rule;

import com.octoperf.kraken.parser.gatling.log.spring.context.ParserContext;
import com.octoperf.kraken.parser.gatling.log.spring.context.LogParserState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
final class CategorySessionRule extends CategoryRule {

  @Autowired
  public CategorySessionRule(final ParserContext parserContext) {
    super(parserContext, LogParserState.SESSION, "Session:");
  }
}
