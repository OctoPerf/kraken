package com.octoperf.kraken.parser.gatling.log.spring.context;

public enum LogParserSubState {
  NONE,
  HEADERS,
  COOKIES,
  STATUS,
  BODY,
}
