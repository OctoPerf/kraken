package com.octoperf.kraken.parser.gatling.log.spring.rule;

import com.octoperf.kraken.analysis.entity.DebugEntry;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

public interface ParserRule extends Predicate<String>, Function<String, Optional<DebugEntry>> {
  default int order() {
    return 0;
  }
}
