package com.kraken.gatling.log.parser.context;

public enum LogParserState {
  VOID,
  AFTER_DELIMITER,
  REQUEST,
  SESSION,
  HTTP_REQUEST,
  HTTP_RESPONSE,
}
