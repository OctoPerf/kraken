package com.kraken.gatling.log.parser.rule;

public interface ContentParserRule extends ParserRule {
  default int order() {
    return 3;
  }
}
