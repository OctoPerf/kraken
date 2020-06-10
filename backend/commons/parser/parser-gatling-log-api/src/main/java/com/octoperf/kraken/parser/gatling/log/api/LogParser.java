package com.octoperf.kraken.parser.gatling.log.api;

import com.octoperf.kraken.analysis.entity.DebugEntry;
import reactor.core.publisher.Flux;

import java.nio.file.Path;

public interface LogParser {

  Flux<DebugEntry> parse (Path logFilePath);

}
