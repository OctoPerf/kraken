package com.kraken.gatling.log.parser.context;

public enum LogParserSubState {
  NONE,
  HEADERS,
  COOKIES,
  STATUS,
  BODY,
}
