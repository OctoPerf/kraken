package com.kraken.gatling.log.parser.rule;

import com.kraken.gatling.log.parser.context.ParserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.kraken.gatling.log.parser.context.LogParserState.HTTP_RESPONSE;
import static com.kraken.gatling.log.parser.context.LogParserSubState.STATUS;
import static java.util.Collections.singletonList;

@Component
final class SubCategoryStatusRule extends SubCategoryRule {

  @Autowired
  public SubCategoryStatusRule(final ParserContext parserContext) {
    super(parserContext, STATUS, singletonList(HTTP_RESPONSE), singletonList("status="));
  }

}
