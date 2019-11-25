package com.kraken.gatling.log.parser.rule;

import com.kraken.gatling.log.parser.context.ParserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.kraken.gatling.log.parser.context.LogParserState.HTTP_REQUEST;
import static com.kraken.gatling.log.parser.context.LogParserSubState.COOKIES;
import static java.util.Collections.singletonList;

@Component
final class SubCategoryCookiesRule extends SubCategoryRule {

  @Autowired
  public SubCategoryCookiesRule(final ParserContext parserContext) {
    super(parserContext, COOKIES, singletonList(HTTP_REQUEST), singletonList("cookies="));
  }

}
