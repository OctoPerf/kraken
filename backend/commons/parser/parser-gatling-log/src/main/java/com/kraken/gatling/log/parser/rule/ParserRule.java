package com.kraken.gatling.log.parser.rule;

import com.kraken.analysis.entity.DebugEntry;

import java.util.Optional;
import java.util.function.*;

public interface ParserRule extends Predicate<String>, Function<String, Optional<DebugEntry>> {
  default int order() {
    return 0;
  }
}
