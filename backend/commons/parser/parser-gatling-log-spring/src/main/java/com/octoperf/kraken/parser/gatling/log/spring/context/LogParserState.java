package com.octoperf.kraken.parser.gatling.log.spring.context;

public enum LogParserState {
  VOID,
  AFTER_DELIMITER,
  REQUEST,
  SESSION,
  HTTP_REQUEST,
  HTTP_RESPONSE,
}
