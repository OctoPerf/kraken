package com.octoperf.kraken.parser.har.api;

import com.octoperf.kraken.analysis.entity.DebugEntry;
import reactor.core.publisher.Flux;

import java.nio.file.Path;

public interface HarParser {

  Flux<DebugEntry> parse(Path harFilePath);

}
