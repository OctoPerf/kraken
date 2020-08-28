package com.octoperf.kraken.parser.gatling.log.spring.rule;

import com.octoperf.kraken.parser.gatling.log.spring.context.LogParserState;
import com.octoperf.kraken.parser.gatling.log.spring.context.LogParserSubState;
import com.octoperf.kraken.parser.gatling.log.spring.context.ParserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static java.util.Collections.singletonList;

@Component
final class SubCategoryCookiesRule extends SubCategoryRule {

  @Autowired
  public SubCategoryCookiesRule(final ParserContext parserContext) {
    super(parserContext, LogParserSubState.COOKIES, singletonList(LogParserState.HTTP_REQUEST), singletonList("cookies="));
  }

}
