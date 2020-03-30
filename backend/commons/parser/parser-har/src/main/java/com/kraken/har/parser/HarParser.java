package com.kraken.har.parser;

import com.kraken.analysis.entity.DebugEntry;
import reactor.core.publisher.Flux;

import java.nio.file.Path;

public interface HarParser {

  Flux<DebugEntry> parse(Path harFilePath);

}
