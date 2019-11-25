package com.kraken.gatling.log.parser.rule;

import com.kraken.gatling.log.parser.context.ParserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.kraken.gatling.log.parser.context.LogParserState.HTTP_REQUEST;
import static com.kraken.gatling.log.parser.context.LogParserSubState.BODY;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

@Component
final class SubCategoryRequestBodyRule extends SubCategoryRule {

  @Autowired
  public SubCategoryRequestBodyRule(final ParserContext parserContext) {
    super(parserContext, BODY, singletonList(HTTP_REQUEST), asList("byteBody=",
        "byteArraysBody=",
        "fileBody=",
        "formBody=",
        "multipartBody="));
  }

}