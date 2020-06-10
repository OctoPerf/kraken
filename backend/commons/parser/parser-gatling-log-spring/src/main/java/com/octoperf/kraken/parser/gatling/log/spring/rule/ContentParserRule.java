package com.octoperf.kraken.parser.gatling.log.spring.rule;

public interface ContentParserRule extends ParserRule {
  default int order() {
    return 3;
  }
}
