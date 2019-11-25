package com.kraken.gatling.log.parser.rule;

import com.kraken.gatling.log.parser.context.ParserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.kraken.gatling.log.parser.context.LogParserState.HTTP_RESPONSE;
import static com.kraken.gatling.log.parser.context.LogParserSubState.BODY;
import static java.util.Collections.singletonList;

@Component
final class SubCategoryResponseBodyRule extends SubCategoryRule {

  @Autowired
  public SubCategoryResponseBodyRule(final ParserContext parserContext) {
    super(parserContext, BODY, singletonList(HTTP_RESPONSE), singletonList("body="));
  }

}
