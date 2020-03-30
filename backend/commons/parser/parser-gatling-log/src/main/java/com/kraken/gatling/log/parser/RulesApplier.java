package com.kraken.gatling.log.parser;

import com.kraken.analysis.entity.DebugEntry;

import java.util.Optional;
import java.util.function.Function;

public interface RulesApplier extends Function<String, Optional<DebugEntry>> {

}
