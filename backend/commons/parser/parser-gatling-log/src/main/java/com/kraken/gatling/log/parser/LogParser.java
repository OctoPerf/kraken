package com.kraken.gatling.log.parser;

import com.kraken.analysis.entity.DebugEntry;
import reactor.core.publisher.Flux;

import java.nio.file.Path;

public interface LogParser {

  Flux<DebugEntry> parse (Path logFilePath);

}
