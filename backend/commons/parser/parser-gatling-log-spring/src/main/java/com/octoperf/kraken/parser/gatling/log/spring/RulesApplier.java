package com.octoperf.kraken.parser.gatling.log.spring;

import com.octoperf.kraken.analysis.entity.DebugEntry;

import java.util.Optional;
import java.util.function.Function;

public interface RulesApplier extends Function<String, Optional<DebugEntry>> {

}
