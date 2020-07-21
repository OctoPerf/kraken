package com.octoperf.kraken.parser.gatling.log.spring.rule;

import com.octoperf.kraken.parser.gatling.log.spring.context.ParserContext;
import com.octoperf.kraken.parser.gatling.log.spring.context.LogParserState;
import com.octoperf.kraken.parser.gatling.log.spring.context.LogParserSubState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

@Component
final class SubCategoryRequestBodyRule extends SubCategoryRule {

  @Autowired
  public SubCategoryRequestBodyRule(final ParserContext parserContext) {
    super(parserContext, LogParserSubState.BODY, singletonList(LogParserState.HTTP_REQUEST), asList("byteBody=",
        "byteArraysBody=",
        "fileBody=",
        "formBody=",
        "multipartBody="));
  }

}