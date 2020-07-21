package com.octoperf.kraken.parser.debug.entry.writer.api;

import com.octoperf.kraken.analysis.entity.DebugEntry;
import reactor.core.publisher.Flux;

public interface DebugEntryWriter {

  Flux<DebugEntry> write(Flux<DebugEntry> entries);

}
