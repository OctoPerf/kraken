package com.kraken.debug.entry.writer;

import com.kraken.analysis.entity.DebugEntry;
import reactor.core.publisher.Flux;

public interface DebugEntryWriter {

  Flux<DebugEntry> write(Flux<DebugEntry> entries);

}
